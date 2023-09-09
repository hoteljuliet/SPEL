package net.hoteljuliet.spel;

import com.github.mustachejava.MustacheFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Step implements Command {

    // todo: each step needs a unique name
    private static final Logger logger = LoggerFactory.getLogger(Step.class);
    private StopWatch stopWatch = new StopWatch();
    public LongAdder success = new LongAdder();
    public LongAdder missing = new LongAdder();
    public LongAdder exceptionThrown = new LongAdder();
    public LongAdder otherFailure = new LongAdder();
    public SummaryStatistics runTimeNanos = new SummaryStatistics();

    // TODO: run in watchdog and calculate overtime
    // public LongAdder overTime = new LongAdder();

    private LimitedCountingMap exceptionsCounter = new LimitedCountingMap();

    public abstract Optional<Boolean> doExecute(Context context) throws Exception;

    public void before() {
        stopWatch.reset();
        stopWatch.start();
    }

    public void after(Optional<Boolean> evaluation) {
        stopWatch.stop();
        runTimeNanos.addValue((double)stopWatch.getNanoTime());
    }

    @Override
    public final Optional<Boolean> execute(Context context) throws Exception {
        Optional<Boolean> retVal = COMMAND_NEITHER;
        try {
            before();
            retVal = doExecute(context);
        }
        catch(Throwable t) {
            throw new RuntimeException(t);
        }
        finally {
            after(retVal);
        }
        return retVal;
    }

    protected void handleException(Exception ex) {
        logger.error("Exception in " + this.getClass().getSimpleName(), ex);
        exceptionThrown.increment();
        String rootCase = ExceptionUtils.getRootCauseMessage(ex);
        exceptionsCounter.put(rootCase);
    }
}
