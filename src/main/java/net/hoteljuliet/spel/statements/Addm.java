package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mustachejava.Mustache;
import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.Step;

import java.io.StringReader;
import java.util.Optional;

public class Addm extends Step {
    private String dest;

    private Mustache mustache;

    @JsonCreator
    public Addm(@JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "exp", required = true) String exp) {
        this.dest = dest;
        this.mustache = mustacheFactory.compile(new StringReader(exp), "");
    }

    @Override
    public Optional<Boolean> execute(Context context) throws Exception {
        try {
            Object value = context.render(mustache);
            context.addField(dest, value);
            success.increment();
        }
        catch(Exception ex) {
            exceptionThrown.increment();
        }
        return Command.COMMAND_NEITHER;
    }
}