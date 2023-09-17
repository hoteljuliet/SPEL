package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.*;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "url")
public class Url extends StepStatement implements Serializable {
    private String source;
    private Action action;

    @JsonCreator
    public Url(@JsonProperty(value = "source", required = true) String source,
               @JsonProperty(value = "action", required = true) Action action) {
        super();
        this.source = source;
        this.action = action;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        if (context.hasField(source)) {
            // TODO: implement
            //     See: https://www.baeldung.com/java-url-encoding-decoding, https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/net/URLCodec.html
        }
        else {
            missingField.increment();
        }
        return StepBase.NEITHER;
    }
}
