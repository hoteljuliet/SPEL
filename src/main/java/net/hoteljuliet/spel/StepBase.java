package net.hoteljuliet.spel;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.Serializable;
import java.util.Optional;

public abstract class StepBase implements Serializable {

    public static final Optional<Boolean> TRUE = Optional.of(true);
    public static final Optional<Boolean> FALSE = Optional.of(false);
    public static final Optional<Boolean> NEITHER = Optional.empty();

    protected String name;
    protected transient StepMetrics stepMetrics;

    public StepBase() {
        ;
    }

    /**
     * Where derived classes put their main processing logic
     * @param context
     * @return
     * @throws Exception
     */
    public abstract Optional<Boolean> doExecute(Context context) throws Exception;

    /**
     * Gives derived classes a chance to implement custom exception handling behavior
     * @param t
     * @param context
     * @return
     */
    protected abstract Optional<Boolean> onException(Throwable t, Context context);

    protected <T> T externalize(Context context, String subKey, Object value, Boolean volatileState) {
        String prefix = volatileState ? "_volatile_state." : "_state.";
        String key = prefix + name.toLowerCase() + "." + subKey;
        if (!context.hasField(key)) {
            context.addField(key, value);
        }
        return context.getField(key);
    }

    protected Boolean requiresExternal(Context context, String subKey, Boolean volatileState) {
        String prefix = volatileState ? "_volatile_state." : "_state.";
        String key = prefix + name.toLowerCase() + "." + subKey;
        return context.hasField(key);
    }

    /**
     * Called before a Step executes, mostly for metric updates
     * @param context
     */
    public final void before(Context context) {

        String metricsKey = "_state.stepMetrics." + name.toLowerCase();
        if (null == stepMetrics) {
            if (!context.hasField(metricsKey)) {
                context.addField(metricsKey, new StepMetrics());
            }
        }
        stepMetrics = context.getField(metricsKey);
        stepMetrics.getStopWatch().start();
    }

    /**
     * Called after a Step executes, mostly for metric updates
     * @param evaluation
     * @param context
     */
    public void after(Optional<Boolean> evaluation, Context context) {
        stepMetrics.getStopWatch().stop();
        stepMetrics.getRunTimeNanos().addValue((double)stepMetrics.getStopWatch().getNanoTime());
        stepMetrics.getLastRunNanos().set(stepMetrics.getStopWatch().getNanoTime());
    }

    /**
     * Step has an execute to make behavior for all other Steps consistent - derived classes just implement doExecute(),
     * while the base classes define before, after, and exception handling.
     * @param context
     * @return
     * @throws Exception
     */
    public final Optional<Boolean> execute(Context context) throws Exception {
        Optional<Boolean> retVal = NEITHER;
        try {
            before(context);
            retVal = doExecute(context);
            stepMetrics.getSuccess().increment();
        }
        catch(Throwable t) {
            stepMetrics.getExceptionThrown().increment();
            String rootCase = ExceptionUtils.getRootCauseMessage(t);
            stepMetrics.getExceptionsCounter().put(rootCase);
            retVal = onException(t, context);
        }
        finally {
            after(retVal, context);
        }
        return retVal;
    }

    public StepMetrics getStepMetrics() {
        return stepMetrics;
    }

    public void evalTrue() {
        stepMetrics.getEvalTrue().increment();
    }

    public void evalFalse() {
        stepMetrics.getEvalFalse().increment();
    }

    public void softFailure() {
        stepMetrics.getSoftFailure().increment();
    }

    public void missingField() {
        stepMetrics.getMissingField().increment();
    }

    public void exceptionThrown() {
        stepMetrics.getExceptionThrown().increment();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
