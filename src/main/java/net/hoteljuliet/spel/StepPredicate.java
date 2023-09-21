package net.hoteljuliet.spel;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public abstract class StepPredicate extends StepBase implements Serializable {

    public final LongAdder evalTrue;
    public final LongAdder evalFalse;
    public List<StepBase> onTrue;
    public List<StepBase> onFalse;
    public StepBase predicate;

    public StepPredicate() {
        evalTrue = new LongAdder();
        evalFalse = new LongAdder();
    }

    /**
     * Increment the true/false counters for this predicate
     * @param evaluation
     * @param context
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

        PredicateResult predicateResult = new PredicateResult(this);
        context.pipelineResult.predicateResults.put(name, predicateResult);
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
        evalTrue.increment();
    }

    public void evalFalse() {
        evalFalse.increment();
    }

}
