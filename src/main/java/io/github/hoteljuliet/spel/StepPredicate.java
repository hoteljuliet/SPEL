package io.github.hoteljuliet.spel;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public abstract class StepPredicate extends StepBase implements Serializable {

    public AtomicLong evalTrue;
    public AtomicLong evalFalse;
    public List<StepBase> onTrue;
    public List<StepBase> onFalse;
    public StepBase predicate;

    public StepPredicate() {
        ;
    }

    /**
     * Increment the true/false counters for this predicate
     * @param evaluation the step's return value for doExecute
     * @param context the context
     */
    @Override
    public void after(Optional<Boolean> evaluation, Context context) {
        super.after(evaluation, context);
        if (evaluation.equals(TRUE)) {
            evalTrue();
        }
        else if (evaluation.equals(FALSE)) {
            evalFalse();
        }
    }

    @Override
    public void toMermaid(Optional<StepBase> parent, Optional<Boolean> predicatePath, StringBuilder stringBuilder) {
        stringBuilder.append(name + "{" + name + "}").append("\n");

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

        StepBase pointer = this;
        for (int i = 0; i < onTrue.size(); i++) {
            if (i == 0) {
                onTrue.get(i).toMermaid(Optional.of(pointer), Optional.of(true), stringBuilder);
            }
            else {
                onTrue.get(i).toMermaid(Optional.of(pointer), Optional.empty(), stringBuilder);
            }
            pointer = onTrue.get(i);
        }

        pointer = this;
        for (int i = 0; i < onFalse.size(); i++) {
            if (i == 0) {
                onFalse.get(i).toMermaid(Optional.of(pointer), Optional.of(false), stringBuilder);
            }
            else {
                onFalse.get(i).toMermaid(Optional.of(pointer), Optional.empty(), stringBuilder);
            }
            pointer = onFalse.get(i);
        }
    }

    public void evalTrue() {
        evalTrue.getAndIncrement();
    }

    public void evalFalse() {
        evalFalse.getAndIncrement();
    }
}
