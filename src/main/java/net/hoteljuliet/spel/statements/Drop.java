package net.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.hoteljuliet.spel.StepBase;
import net.hoteljuliet.spel.Context;
import net.hoteljuliet.spel.StepStatement;
import net.hoteljuliet.spel.Step;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "drop")
public class Drop extends StepStatement implements Serializable {
    @JsonCreator
    public Drop() {
    }
    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        context.addField("_collect", false);
        return StepBase.EMPTY;
    }
}
