package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.*;
import net.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.Optional;

/**
 * Fetch a Map String, Object or List Object from a URL, and add it to the context at the given destination.
 */
@Step(tag = "add-http")
public class AddHttp extends StepStatement implements Serializable {
    private final TemplateLiteral url;
    private final TemplateLiteral headers;
    private final TemplateLiteral body;
    private final TemplateLiteral dest;

    @JsonCreator
    public AddHttp(@JsonProperty(value = "url", required = true) TemplateLiteral url,
                   @JsonProperty(value = "headers", required = true) TemplateLiteral headers,
                   @JsonProperty(value = "body", required = true) TemplateLiteral body,
                   @JsonProperty(value = "dest", required = true) TemplateLiteral dest) {
        super();
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        return EMPTY;
    }
}
