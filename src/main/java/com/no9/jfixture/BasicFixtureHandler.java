package com.no9.jfixture;

import java.util.Map;

public abstract class BasicFixtureHandler extends FixtureHandler {
    public abstract void process(Map<String, Object> fixtureInput) throws FixtureException;

    @Override
    public final void process(Fixtures fixtures, Map<String, Object> fixtureInput) throws FixtureException {
        process(fixtureInput);
    }
}
