package com.no9.jfixture;

import java.io.Closeable;
import java.util.Map;

public interface FixtureHandler extends Closeable {
    boolean canProcess(Map<String, Object> fixtureInput);
}
