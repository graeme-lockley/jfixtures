package com.no9.jfixture;

import java.util.Map;

public interface BasicFixtureHandler extends FixtureHandler {
    void process(Map<String, Object> fixtureInput) throws FixtureException;
}
