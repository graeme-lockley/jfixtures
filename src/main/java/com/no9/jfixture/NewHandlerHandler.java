package com.no9.jfixture;

import java.io.IOException;
import java.util.Map;

public class NewHandlerHandler implements ExtendedFixtureHandler {
    public static final String NEW_HANDLER_SELECTOR = "new-handler";

    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return fixtureInput.containsKey(NEW_HANDLER_SELECTOR);
    }

    @Override
    public void process(Fixtures fixtures, Map<String, Object> fixtureInput) throws FixtureException {
        try {
            fixtures.addHandler((FixtureHandler) Class.forName(String.valueOf(fixtureInput.get(NEW_HANDLER_SELECTOR))).newInstance());
        } catch (InstantiationException e) {
            throw new FixtureException(e);
        } catch (IllegalAccessException e) {
            throw new FixtureException(e);
        } catch (ClassNotFoundException e) {
            throw new FixtureException(e);
        }
    }

    @Override
    public void close() throws IOException {
        // nothing to close as no resources are opened
    }
}
