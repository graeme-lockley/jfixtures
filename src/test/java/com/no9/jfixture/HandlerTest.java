package com.no9.jfixture;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class HandlerTest {
    protected static Map<String, Object> parseContent(String content) {
        Yaml yaml = new Yaml();

        return (Map<String, Object>) yaml.load(content);
    }

    protected static boolean hasHandler(Fixtures fixtures, Class handlerClass) {
        for (FixtureHandler handler : fixtures.handlers()) {
            if (handlerClass.isInstance(handler)) {
                return true;
            }
        }

        return false;
    }
}
