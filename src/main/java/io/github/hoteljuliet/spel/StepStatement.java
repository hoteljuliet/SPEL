package io.github.hoteljuliet.spel;

import java.io.Serializable;
import java.util.Optional;

public abstract class StepStatement extends StepBase implements Serializable {

    public StepStatement() {
        super();
    }

    @Override
    public void toMermaid(Optional<StepBase> parent, Optional<Boolean> predicatePath, StringBuilder stringBuilder) {

        String text = String.format("%s[%s\\nsoft:%d\\nex:%d\\navg:%d]", name, name, softFailure.get(), exception.get(), avgNs.get());
        stringBuilder.append(text).append("\n");

        if (invocations.get() > 0) {
            Double successRate = (success.doubleValue() - softFailure.doubleValue() / invocations.doubleValue());
            String color = failureRateColor(successRate);
            stringBuilder.append("style " + name + " fill:" + color).append("\n");
        }

        if (parent.isPresent()) {
            // if parent is a predicate
            if (predicatePath.isPresent()) {
                StepPredicate stepPredicate = (StepPredicate) parent.get();
                if (predicatePath.get()) {
                    stringBuilder.append(parent.get().name + "-- true(" + stepPredicate.evalTrue.longValue() + ") -->" + name).append("\n");
                }
                else {
                    stringBuilder.append(parent.get().name + "-- false(" + stepPredicate.evalFalse.longValue() + ") -->" + name).append("\n");
                }
            }
            // else parent is another statement
            else {
                stringBuilder.append(parent.get().name + " --> " + name).append("\n");
            }
        }
        else {
            ;
        }
    }

    private String failureRateColor(Double successRate) {
        if (successRate == 1) return "#859900";
        if (successRate > .99) return "#b58900";
        if (successRate > .98) return "#cb4b16";
        if (successRate > .97) return "#d33682";
        return "#dc322f";
    }
}
