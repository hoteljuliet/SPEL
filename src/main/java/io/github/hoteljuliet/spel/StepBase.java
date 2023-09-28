package io.github.hoteljuliet.spel;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public abstract class StepBase implements Serializable {

    public static final Optional<Boolean> TRUE = Optional.of(true);
    public static final Optional<Boolean> FALSE = Optional.of(false);
    public static final Optional<Boolean> EMPTY = Optional.empty();

    protected String name;
    public final StopWatch stopWatch;

    public AtomicLong totalNs;
    public AtomicLong maxNs;
    public AtomicLong avgNs;
    public AtomicLong invocations;
    public AtomicLong success;
    public AtomicLong exception;
    public AtomicLong softFailure;

    public StepBase() {
        stopWatch = new StopWatch();
    }

    // TODO: document the need to synchronize doExecute() and clear() consider making this method synchronized
    public void clear() {
        ;
    }

    /**
     * Where derived classes put their main processing logic
     * @param context
     * @return true, false, or empty
     * @throws Exception exception
     */
    public abstract Optional<Boolean> doExecute(Context context) throws Exception;

    /**
     * Called before a Step executes, mostly for metric updates
     * @param context the context
     */
    public final void before(Context context) {
        stopWatch.reset();
        stopWatch.start();
        invocations.getAndIncrement();
    }

    /**
     * Called after a Step executes, mostly for metric updates
     * @param evaluation step's return from doExecute
     * @param context the context
     */
    public void after(Optional<Boolean> evaluation, Context context) {
        stopWatch.stop();
        Long currentNs = stopWatch.getNanoTime();
        maxNs.set(Math.max(currentNs, maxNs.get()));
        totalNs.getAndAdd(currentNs);
        avgNs.set(totalNs.get() / invocations.get());
    }

    /**
     * Step has an execute to make behavior for all other Steps consistent - derived classes just implement doExecute(),
     * while the base classes define before, after, and exception handling.
     * @param context context
     * @return true, false, or empty
     * @throws Exception exception
     */
    public final Optional<Boolean> execute(Context context) throws Exception {
        Optional<Boolean> retVal = EMPTY;
        try {
            before(context);
            invocations.getAndIncrement();
            retVal = doExecute(context);
            success.getAndIncrement();
        }
        catch(Throwable t) {
            exception.getAndIncrement();
        }
        finally {
            after(retVal, context);
        }
        return retVal;
    }

    public void softFailure() {
        softFailure.getAndIncrement();
    }

    public abstract void toMermaid(Optional<StepBase> parent, Optional<Boolean> predicatePath, StringBuilder stringBuilder);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
