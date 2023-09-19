package net.hoteljuliet.spel;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public abstract class StepBase implements Serializable {

    public static final Optional<Boolean> TRUE = Optional.of(true);
    public static final Optional<Boolean> FALSE = Optional.of(false);
    public static final Optional<Boolean> EMPTY = Optional.empty();

    protected String name;
    public final StopWatch stopWatch;
    public final SummaryStatistics runTimeNanos;
    public final LongAdder success;
    public final LongAdder failure;
    public final LimitedCountingMap exceptionsCounter;

    public StepBase() {
        stopWatch = new StopWatch();
        runTimeNanos = new SummaryStatistics();
        success = new LongAdder();
        failure = new LongAdder();
        exceptionsCounter = new LimitedCountingMap();
    }

    public void clear() {
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

    /**
     * Called before a Step executes, mostly for metric updates
     * @param context
     */
    public final void before(Context context) {
        stopWatch.start();
    }

    /**
     * Called after a Step executes, mostly for metric updates
     * @param evaluation
     * @param context
     */
    public void after(Optional<Boolean> evaluation, Context context) {
        stopWatch.stop();
        runTimeNanos.addValue((double)stopWatch.getNanoTime());
    }

    /**
     * Step has an execute to make behavior for all other Steps consistent - derived classes just implement doExecute(),
     * while the base classes define before, after, and exception handling.
     * @param context
     * @return
     * @throws Exception
     */
    public final Optional<Boolean> execute(Context context) throws Exception {
        Optional<Boolean> retVal = EMPTY;
        try {
            before(context);
            retVal = doExecute(context);
            success.increment();
        }
        catch(Throwable t) {
            String rootCase = ExceptionUtils.getRootCauseMessage(t);
            exceptionsCounter.put(rootCase);
            retVal = onException(t, context);
        }
        finally {
            after(retVal, context);
        }
        return retVal;
    }

    public void failure() {
        failure.increment();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
