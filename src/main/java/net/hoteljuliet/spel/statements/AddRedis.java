package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.*;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.Serializable;
import java.util.*;

@Step(tag = "add-redis")
public class AddRedis extends StepStatement implements Serializable {

    private final Map<String, Object> config;
    private final String key;
    private final String dest;
    private transient RedissonClient client;

    @JsonCreator
    public AddRedis(@JsonProperty(value = "config", required = true) Map<String, Object> config,
                    @JsonProperty(value = "key", required = true) String key,
                    @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.config = config;
        this.key = key;
        this.dest = dest;
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
        context.addField(dest, addedMap);
        return EMPTY;
    }
}
