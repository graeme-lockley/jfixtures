package com.no9.jfixture;

import org.junit.Test;

import java.io.IOException;

public class FixturesTest {
    @Test(expected = java.io.IOException.class)
    public void should_fail_because_resource_does_not_exist() throws IOException {
        Fixtures.loadFromResources("unknown.yaml");
    }

    @Test
    public void should_be_able_to_load_a_simple_fixtures_file() throws IOException {
        Fixtures.loadFromResources("initial.yaml");
    }

    @Test(expected = com.no9.jfixture.FixtureException.class)
    public void should_fail_because_a_fixture_has_multiple_selectors() throws IOException, FixtureException {
        try (Fixtures fixtures = Fixtures.fromString("- {objective: peace, place: world}")) {
            fixtures.processFixtures();
        }
    }

    @Test(expected = com.no9.jfixture.FixtureException.class)
    public void should_fail_because_the_fixtures_are_not_a_sequence() throws IOException, FixtureException {
        try (Fixtures fixtures = Fixtures.fromString("objective: peace\nplace: world")) {
            fixtures.processFixtures();
        }
    }

    @Test(expected = com.no9.jfixture.FixtureException.class)
    public void should_be_unable_process_the_fixtures_as_the_handler_is_not_registered() throws IOException, FixtureException {
        try (Fixtures fixtures = Fixtures.loadFromResources("initial.yaml")) {
            fixtures.processFixtures();
        }
    }
}
