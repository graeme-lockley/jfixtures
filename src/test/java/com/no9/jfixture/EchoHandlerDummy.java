package com.no9.jfixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EchoHandlerDummy implements FixtureHandler {
    private List<String> messages = new ArrayList<String>();

    public static EchoHandlerDummy create() {
        return new EchoHandlerDummy();
    }

    public List<String> messages() {
        return messages;
    }

    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return fixtureInput.containsKey("echo");
    }

    @Override
    public void process(Map<String, Object> fixtureInput) throws FixtureException {
        messages.add(String.valueOf(fixtureInput.get("echo")));
    }
}