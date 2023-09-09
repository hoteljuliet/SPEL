package net.hoteljuliet.spel;

import com.github.mustachejava.MustacheFactory;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.operator.Operator;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Step implements Command {

    private StopWatch stopWatch = new StopWatch();

    public void before() {
        stopWatch.reset();
        stopWatch.start();
    }

    public void after() {
        stopWatch.stop();
        runTimeNanos.addValue((double)stopWatch.getNanoTime());
    }

    public abstract Optional<Boolean> doExecute(Context context) throws Exception;

    @Override
    public final Optional<Boolean> execute(Context context) throws Exception {
        Optional<Boolean> retVal;
        try {
            before();
            retVal = doExecute(context);
        }
        catch(Throwable t) {
            throw new RuntimeException(t);
        }
        finally {
            after();
        }
        return retVal;
    }


    private final static Pattern mustachePattern = Pattern.compile("\\{\\{(.+?)\\}\\}");

    protected MustacheFactory mustacheFactory = new UnescapedMustacheFactory();

    public LongAdder success = new LongAdder();
    public LongAdder missing = new LongAdder();
    public LongAdder exceptionThrown = new LongAdder();

    // TODO
    // public LongAdder overTime = new LongAdder();
    public LongAdder otherFailure = new LongAdder();

    public SummaryStatistics runTimeNanos = new SummaryStatistics();

    public LongAdder evalTrue = new LongAdder();
    public LongAdder evalFalse = new LongAdder();

    private LimitedCountingMap exceptionsCounter = new LimitedCountingMap();

    protected void handleException(Exception ex) {
        exceptionThrown.increment();
        String rootCase = ExceptionUtils.getRootCauseMessage(ex);
        exceptionsCounter.put(rootCase);
    }

    public Set<String> findVariables(String expression) {
        Set<String> variables = new HashSet<>();
        Matcher matcher = mustachePattern.matcher(expression);

        while (matcher.find()) {
            variables.add(matcher.group(1));
        }

        return variables;
    }

}
