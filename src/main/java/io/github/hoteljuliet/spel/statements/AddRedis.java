package io.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Parser;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepStatement;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Add a Map from Redis
 */
@Step(tag = "add-redis")
public class AddRedis extends StepStatement implements Serializable {

    private final Map<String, Object> config;
    private final String key;
    private final String out;
    private transient RedissonClient client;


    /**
     * Add a Map from Redis to the context
     * @param config Redisson client config
     * @param key the key of the Map in Redis
     * @param out the path into the context where the map will be added
     */
    @JsonCreator
    public AddRedis(@JsonProperty(value = "config", required = true) Map<String, Object> config,
                    @JsonProperty(value = "key", required = true) String key,
                    @JsonProperty(value = "out", required = true) String out) {
        super();
        this.config = config;
        this.key = key;
        this.out = out;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        if (null == client) {
            Config rConfig = Config.fromYAML(Parser.yamlMapper.writeValueAsString(config));
            client = Redisson.create(rConfig);
        }

        RMap<String, Object> redisMap = client.getMap(key);
        HashMap<String, Object> addedMap = new HashMap<>();
        addedMap.putAll(redisMap);
        context.addField(out, addedMap);
        return EMPTY;
    }
}
