package com.no9.jfixture;

import java.io.IOException;
import java.util.Map;

import static com.no9.jfixture.YAMLDSL.fromYAML;

public class ImportHandler implements ExtendedFixtureHandler {
    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return fixtureInput.containsKey("import");
    }

    @Override
    public void close() throws IOException {
        // nothing to close as no resources are opened
    }

    @Override
    public void process(Fixtures fixtures, Map<String, Object> fixtureInput) throws FixtureException {
        String importLocation = fromYAML(fixtureInput).map().field("import").asString();

        FixturesInput input = FixturesInput.none();
        try {
            input = FixturesInput.fromLocation(importLocation);
            fixtures.processFixtures(input);
        } catch (IOException ex) {
            throw new FixtureException(ex);
        } finally {
            try {
                input.close();
            } catch (IOException ignored) {
                // Swallow exception...
            }
        }
    }
}
