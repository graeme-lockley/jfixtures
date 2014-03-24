package za.co.no9.jfixture;

import java.util.Map;

public class EchoHandler extends BasicFixtureHandler {
    public static final String ECHO_SELECTOR = "echo";

    public static EchoHandler create() {
        return new EchoHandler();
    }

    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return fixtureInput.containsKey(ECHO_SELECTOR);
    }

    @Override
    public void process(Map<String, Object> fixtureInput) throws FixtureException {
        System.out.println(String.valueOf(fixtureInput.get(ECHO_SELECTOR)));
    }
}
