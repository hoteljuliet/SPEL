package com.github.hoteljuliet.spel.statements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.hoteljuliet.spel.Context;
import com.github.hoteljuliet.spel.Step;
import com.github.hoteljuliet.spel.StepBase;
import com.github.hoteljuliet.spel.StepStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Uses a reference to the pipeline to find another step and call it's clear method.
 * Useful for scenarios where a step has state but we want to clear it on a window's close.
 */
@Step(tag = "clear")
public class Clear extends StepStatement implements Serializable {
    private List<String> names;

    @JsonCreator
    public Clear(@JsonProperty(value = "names", required = true) List<String> names) {
        super();
        this.names = names;
    }

    @Override
    public Optional<Boolean> doExecute(Context context) throws Exception {
        for (String name : names) {
            Optional<StepBase> value = context.pipeline.find(name);
            if (value.isPresent()) {
                value.get().clear();
            }
            else {
                softFailure();
            }
        }
        return EMPTY;
    }
}

