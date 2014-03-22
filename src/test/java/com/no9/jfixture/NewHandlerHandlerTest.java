package com.no9.jfixture;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NewHandlerHandlerTest {
    private Fixtures fixtures;

    @Test
    public void should_auto_register_the_handler_handler() throws IOException {
        fixtures = Fixtures.loadFromResources("initial.yaml");
        assertTrue(hasHandler(NewHandlerHandler.class));
    }

    @Test
    public void should_confirm_that_a_handler_was_added_following_a_use_handler() throws IOException, FixtureException {
        fixtures = Fixtures.fromString("- new-handler: com.no9.jfixture.EchoHandlerDummy");

        assertFalse(hasHandler(EchoHandlerDummy.class));

        fixtures.processFixtures();

        assertTrue(hasHandler(EchoHandlerDummy.class));
    }

    private boolean hasHandler(Class handlerClass) {
        for (FixtureHandler handler : fixtures.handlers()) {
            if (handlerClass.isInstance(handler)) {
                return true;
            }
        }

        return false;
    }
}
