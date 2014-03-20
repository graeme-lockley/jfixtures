package com.no9.jfixture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EchoHandlerDummy implements BasicFixtureHandler {
    private List<String> messages = new ArrayList<String>();
    private boolean hasBeenClosed = false;

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

    @Override
    public void close() throws IOException {
        hasBeenClosed = true;
    }

    public boolean hasBeenClosed() {
        return hasBeenClosed;
    }
}
