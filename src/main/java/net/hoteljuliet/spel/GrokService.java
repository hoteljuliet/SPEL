package net.hoteljuliet.spel;

import java.util.List;

public class GrokService {

    private static GrokService instance;

    public static GrokService getInstance(List<String> directories) {
        if (null == instance) {
            instance = new GrokService(directories);
        }
        return instance;
    }

    private GrokService(List<String> directories) {




    }
}
