package net.hoteljuliet.spel;

import java.util.HashMap;

public class State {

    public static String INPUT = "_input";
    public static String OUTPUT = "_output";
    public static String ON_TIMER = "_on_timer";
    public static String GET_RESULT = "_get_result";
    public static String MERGE = "_merge";
    public static String STATE = "_state";
    public static String STEP_METRICS = "_state.stepMetrics";
    public static String VOLATILE = "_volatile_state";
    public static String getStateKey(String name, String subKey, Boolean volatileState) {
        String prefix = volatileState ? VOLATILE + "." : STATE + ".";
        return prefix + name.toLowerCase() + "." + subKey;
    }
}
