package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mustachejava.Mustache;
import net.hoteljuliet.spel.Command;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.SpelUtils;

import java.io.Serializable;
import java.util.Optional;

public class AddM extends StatementStep implements Serializable {
    private String dest;
    private String exp;

    private transient Mustache mustache;

    @JsonCreator
    public AddM(@JsonProperty(value = "dest", required = true) String dest,
                @JsonProperty(value = "exp", required = true) String exp) {
        this.dest = dest;
        this.exp = exp;
        this.mustache = SpelUtils.compile(exp);
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        Object value = context.render(mustache);
        context.addField(dest, value);
        return Command.COMMAND_NEITHER;
    }

    @Override
    public void restore() {
        super.restore();
        this.mustache = SpelUtils.compile(exp);
    }
}