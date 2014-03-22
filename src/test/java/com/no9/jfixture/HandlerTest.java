package com.no9.jfixture;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class HandlerTest {
    protected static Map<String, Object> parseContent(String content) {
        Yaml yaml = new Yaml();

        return (Map<String, Object>) yaml.load(content);
    }
}
