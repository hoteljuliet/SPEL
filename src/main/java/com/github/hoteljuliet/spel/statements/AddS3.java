package com.github.hoteljuliet.spel.statements;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Parser;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepStatement;
import com.github.hoteljuliet.spel.mustache.TemplateLiteral;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * perform a get object that returns json, turn into a map, and add to context at dest
 */
@Step(tag = "add-s3")
public class AddS3 extends StepStatement implements Serializable {

    private final Map<String, Object> config;
    private final TemplateLiteral bucket;
    private final TemplateLiteral key;
    private final TemplateLiteral region;
    private final String dest;
    private transient AmazonS3 amazonS3;

    @JsonCreator
    public AddS3(@JsonProperty(value = "config", required = true) Map<String, Object> config,
                 @JsonProperty(value = "bucket", required = true) TemplateLiteral bucket,
                 @JsonProperty(value = "key", required = true) TemplateLiteral key,
                 @JsonProperty(value = "region", required = true) TemplateLiteral region,
                 @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.config = config;
        this.bucket = bucket;
        this.key = key;
        this.region = region;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (null == amazonS3) {
            String s3Region = region.get(context);
            amazonS3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new DefaultAWSCredentialsProviderChain())
                    .withRegion(Regions.valueOf(s3Region))
                    .build();
        }

        // using template literal allows the user to target objects based on the contents of the context
        String s3Bucket = bucket.get(context);
        String s3Key = key.get(context);
        S3Object s3Object = amazonS3.getObject(s3Bucket, s3Key);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        Map<String, Object> addedMap = Parser.jsonMapper.readValue(s3ObjectInputStream, Map.class);
        context.addField(dest, addedMap);
        s3ObjectInputStream.close();
        return EMPTY;
    }
}
