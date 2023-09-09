package net.hoteljuliet.spel;

import java.util.Optional;

public interface Command {
    public static final Optional<Boolean> COMMAND_TRUE = Optional.of(true);
    public static final Optional<Boolean> COMMAND_FALSE = Optional.of(false);

    public static final Optional<Boolean> COMMAND_NEITHER = Optional.empty();

    public Optional<Boolean> execute(Context context) throws Exception;
}
