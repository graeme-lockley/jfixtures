package com.no9.jfixture;

import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Fixtures implements Closeable {
    private InputStream inputStream;
    private Iterable<Object> fixtures;

    private Fixtures(InputStream inputStream) throws IOException {
        Yaml yaml = new Yaml();

        this.inputStream = inputStream;
        this.fixtures = yaml.loadAll(inputStream);
    }

    public static Fixtures loadFromResources(String resourceName) throws IOException {
        ClassLoader loader = Fixtures.class.getClassLoader();
        InputStream inputStream = loader.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IOException("The resource " + resourceName + " could not be accessed.");
        }
        return new Fixtures(inputStream);
    }

    public static Fixtures fromString(String fixtureContent) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(fixtureContent.getBytes());
        return new Fixtures(inputStream);
    }

    public void processFixtures() throws FixtureException {
        for (Object object : fixtures) {
            if (object instanceof List) {
                for (Map<String, Object> fixture : ((List<Map<String, Object>>) object)) {
                    if (fixture.size() == 1) {
                        FixtureHandler handler = locateHandler(fixture);
                        if (handler == null) {
                            throw new FixtureException("No fixture handler has been registered to support " + fixture.toString());
                        }
                    } else {
                        throw new FixtureException("Each fixture must have a single selector.");
                    }
                }
            } else {
                throw new FixtureException("The top-level of a fixture control file must be a sequence.");
            }
        }
    }

    private FixtureHandler locateHandler(Map<String, Object> fixture) {
        return null;
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
            inputStream = null;
        }
    }
}
