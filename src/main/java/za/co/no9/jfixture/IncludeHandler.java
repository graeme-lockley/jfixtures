package za.co.no9.jfixture;

import java.io.IOException;
import java.util.Map;

import static za.co.no9.jfixture.YAMLDSL.fromYAML;

public class IncludeHandler extends FixtureHandler {
    public static final String INCLUDE_SELECTOR = "include";

    @Override
    public boolean canProcess(Map<String, Object> fixtureInput) {
        return fixtureInput.containsKey(INCLUDE_SELECTOR);
    }

    @Override
    public void process(Fixtures fixtures, Map<String, Object> fixtureInput) throws FixtureException {
        String importLocation = fromYAML(fixtureInput).map().field(INCLUDE_SELECTOR).asString();

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
