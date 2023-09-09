package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.hoteljuliet.spel.Context;

import java.util.List;
import java.util.Optional;

public class Remove extends StatementStep {
    private List<String> sources;
    @JsonCreator
    public Remove(@JsonProperty(value = "sources", required = true) List<String> sources) {
        this.sources = sources;
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {

        try {
            for (String path : sources) {
                if (context.hasField(path)) {
                    context.removeField(path);
                    success.increment();
                }
                else {
                    missing.increment();
                }
            }
        }
        catch(Exception ex) {
            handleException(ex);
        }
        return COMMAND_NEITHER;
    }
}


