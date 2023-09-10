package net.hoteljuliet.spel;

import org.apache.commons.lang3.time.StopWatch;

import java.util.Optional;

public abstract class Step implements Command {

    protected StopWatch stopWatch = new StopWatch();

    public abstract Optional<Boolean> doExecute(Context context) throws Exception;

    protected String name;

    /**
     * Called before a Step executes, mostly for metric updates
     * @param context
     */
    public void before(Context context) {
        if (!context.metricsPerStep.containsKey(name)) {
            context.metricsPerStep.put(name, new StepMetrics());
        }
        stopWatch.reset();
        stopWatch.start();
    }

    /**
     * Called after a Step executes, mostly for metric updates
     * @param evaluation
     * @param context
     */
    public void after(Optional<Boolean> evaluation, Context context) {
        stopWatch.stop();
        context.getMetrics(name).runTimeNanos.addValue((double)stopWatch.getNanoTime());
        context.getMetrics(name).lastRunNanos.set(stopWatch.getNanoTime());

        if (evaluation.equals(COMMAND_TRUE)) {
            context.getMetrics(name).evalTrue.increment();
        }
        else if (evaluation.equals(COMMAND_FALSE)) {
            context.getMetrics(name).evalFalse.increment();
        }
    }

    /**
     * Step has an execute to make behavior for all other Steps consistent - derived classes just implement doExecute(),
     * while the base classes define before, after, and exception handling.
     * @param context
     * @return
     * @throws Exception
     */
    @Override
    public final Optional<Boolean> execute(Context context) throws Exception {
        Optional<Boolean> retVal = COMMAND_NEITHER;
        try {
            before(context);
            retVal = doExecute(context);
            context.getMetrics(name).success.increment();
        }
        catch(Throwable t) {
            retVal = handleException(t, context);
        }
        finally {
            after(retVal, context);
        }
        return retVal;
    }

    /**
     * Behavior deferred to derived classes
     * @param t
     * @param context
     * @return
     */
    protected abstract Optional<Boolean> handleException(Throwable t, Context context);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
