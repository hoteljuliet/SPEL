package io.github.hoteljuliet.spel.predicates;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.hoteljuliet.spel.Context;
import io.github.hoteljuliet.spel.Step;
import io.github.hoteljuliet.spel.StepPredicate;
import io.github.hoteljuliet.spel.mustache.TemplateLiteral;

import java.io.Serializable;
import java.util.Optional;

@Step(tag = "crunch")
public class Crunch extends StepPredicate implements Serializable {


    //private final String expression;
    //private final List<String> variables;
    //private transient CompiledExpression compiledExpression;

    @JsonCreator
    public Crunch(@JsonProperty(value = "exp", required = true) TemplateLiteral exp) {
        super();
        /*
        this.variables = exp.getVariables();
        String temp = exp.toString();
        for (int i = 0; i < variables.size(); i++) {
            String regex = "\\{\\{(" + variables.get(i) + ")\\}\\}";
            String replacement = "\\$" + String.valueOf(i +1);
            temp = temp.replaceAll(regex, replacement);
        }
        this.expression = temp;
         */
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        /*

        if (null == compiledExpression) compiledExpression = compileExpression(expression);

        double[] vars = new double[variables.size()];
        for (int i = 0; i < vars.length; i++) {
            String variable = variables.get(i);
            Object fieldValue = context.getField(variable);
            Double value = Doubles.tryParse(String.valueOf(fieldValue));
            vars[i] = value;
        }
        double result = compiledExpression.evaluate(vars);
        return (result == 1.0) ? TRUE : FALSE;
         */
        return null;
    }
}