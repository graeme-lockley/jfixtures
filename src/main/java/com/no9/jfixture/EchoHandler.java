package com.no9.jfixture;

import java.util.Map;

public class EchoHandler implements FixtureHandler {
    public static EchoHandler create() {
        return new EchoHandler();
    }

    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return fixtureInput.containsKey("echo");
    }

    @Override
    public void process(Map<String, Object> fixtureInput) throws FixtureException {
        System.out.println(String.valueOf(fixtureInput.get("echo")));
    }
}
