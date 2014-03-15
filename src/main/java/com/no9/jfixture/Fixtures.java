package com.no9.jfixture;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

public class Fixtures {
    private Iterable<Object> fixtures;

    private Fixtures(Iterable<Object> fixtures) {
        this.fixtures = fixtures;
    }

    public static Fixtures loadFromResources(String resourceName) throws IOException {
        ClassLoader loader = Fixtures.class.getClassLoader();
        try (InputStream is = loader.getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IOException("The resource " + resourceName + " could not be accessed.");
            }

            Yaml yaml = new Yaml();
            return new Fixtures(yaml.loadAll(is));
        }
    }
}
