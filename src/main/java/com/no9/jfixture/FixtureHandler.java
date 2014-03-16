package com.no9.jfixture;

import java.util.Map;

public interface FixtureHandler {
    boolean canProcess(Map<String, Object> fixtureInput);

    void process(Map<String, Object> fixtureInput) throws FixtureException;
}
