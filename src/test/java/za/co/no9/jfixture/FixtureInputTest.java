package za.co.no9.jfixture;

import org.junit.Test;

import java.io.IOException;

public class FixtureInputTest {
    @Test(expected = java.io.IOException.class)
    public void should_fail_because_resource_does_not_exist() throws IOException {
        FixturesInput.fromResources("unknown.yaml");
    }

    @Test
    public void should_be_able_to_load_a_simple_fixtures_file() throws IOException {
        FixturesInput.fromResources("initial.yaml");
    }
}
