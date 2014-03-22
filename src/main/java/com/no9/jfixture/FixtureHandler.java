package com.no9.jfixture;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public abstract class FixtureHandler implements Closeable {
    abstract boolean canProcess(Map<String, Object> fixtureInput);

    abstract void process(Fixtures fixtures, Map<String, Object> fixtureInput) throws FixtureException;

    @Override
    public void close() throws IOException {
    }
}
