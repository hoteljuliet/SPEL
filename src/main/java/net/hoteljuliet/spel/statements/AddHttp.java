package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.BaseStep;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "add-http")
public class AddHttp extends StatementBaseStep implements Serializable {
    private final String url;
    private final String headers;
    private final String body;
    private final String dest;

    @JsonCreator
    public AddHttp(@JsonProperty(value = "url", required = true) String url,
                   @JsonProperty(value = "headers", required = true) String headers,
                   @JsonProperty(value = "body", required = true) String body,
                   @JsonProperty(value = "dest", required = true) String dest) {
        super();
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.dest = dest;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasFields(url, headers, body)) {


        }
        else {
            missingField.increment();
        }
        return BaseStep.NEITHER;
    }
}
