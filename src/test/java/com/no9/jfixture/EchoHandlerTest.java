package com.no9.jfixture;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class EchoHandlerTest extends HandlerTest {
    private EchoHandler echoHandler = EchoHandler.create();

    @Test
    public void should_match_if_the_fixture_input_has_the_echo_selector() {
        assertTrue(echoHandler.canProcess(parseContent("echo: Hello World\n")));
    }

    @Test
    public void should_not_match_if_the_fixture_input_does_not_have_the_echo_selector() {
        assertFalse(echoHandler.canProcess(parseContent("echos: Hello World\n")));
    }

    @Test
    public void should_take_the_fixture_input_content_and_drop_into_stdout() throws FixtureException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        PrintStream oldOut = System.out;

        System.setOut(printStream);

        echoHandler.process(parseContent("echo: Hello World"));

        printStream.close();
        System.setOut(oldOut);

        assertEquals("Hello World", baos.toString().trim());
    }
}
