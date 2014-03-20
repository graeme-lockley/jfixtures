package com.no9.jfixture;

import java.util.Map;

public interface ExtendedFixtureHandler extends FixtureHandler {
    void process(Fixtures fixtures, Map<String, Object> fixtureInput) throws FixtureException;
}
