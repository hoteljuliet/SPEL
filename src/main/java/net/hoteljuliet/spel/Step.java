package net.hoteljuliet.spel;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.atomic.LongAdder;

public abstract class Step implements Command {

    protected static final Logger logger = LoggerFactory.getLogger(Step.class);

    protected StopWatch stopWatch = new StopWatch();

    public abstract Optional<Boolean> doExecute(Context context) throws Exception;

    protected String name;

    public void before(Context context) {
        if (!context.metricsPerStep.containsKey(name)) {
            context.metricsPerStep.put(name, new StepMetrics());
        }
        stopWatch.reset();
        stopWatch.start();
    }

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

    protected abstract Optional<Boolean> handleException(Throwable t, Context context);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
