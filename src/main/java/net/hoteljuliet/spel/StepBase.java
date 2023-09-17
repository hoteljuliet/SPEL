package net.hoteljuliet.spel;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public abstract class StepBase implements Serializable {

    public static final Optional<Boolean> TRUE = Optional.of(true);
    public static final Optional<Boolean> FALSE = Optional.of(false);
    public static final Optional<Boolean> NEITHER = Optional.empty();

    protected transient StopWatch stopWatch;

    protected String name;
    protected SummaryStatistics runTimeNanos;
    protected AtomicLong lastRunNanos;
    protected LongAdder success = new LongAdder();
    protected LongAdder missingField = new LongAdder();
    protected LongAdder exceptionThrown = new LongAdder();
    protected LongAdder softFailure = new LongAdder();
    protected LimitedCountingMap exceptionsCounter = new LimitedCountingMap();

    protected transient Boolean initialized;

    public StepBase() {
        stopWatch = new StopWatch();
        runTimeNanos = new SummaryStatistics();
        lastRunNanos = new AtomicLong();
        success = new LongAdder();
        missingField = new LongAdder();
        exceptionThrown = new LongAdder();
        softFailure = new LongAdder();
        exceptionsCounter = new LimitedCountingMap();
        initialized = true;
    }

    public void reinitialize() {
        stopWatch = new StopWatch();
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

        if (null == initialized || false == initialized) {
             reinitialize();
             initialized = true;
        }

        if (!context.getExecutedBaseSteps().contains(this)) {
            context.getExecutedBaseSteps().add(this);
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
        runTimeNanos.addValue((double)stopWatch.getNanoTime());
        lastRunNanos.set(stopWatch.getNanoTime());
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
            this.success.increment();
        }
        catch(Throwable t) {
            exceptionThrown.increment();
            String rootCase = ExceptionUtils.getRootCauseMessage(t);
            exceptionsCounter.put(rootCase);
            retVal = onException(t, context);
        }
        finally {
            after(retVal, context);
        }
        return retVal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
