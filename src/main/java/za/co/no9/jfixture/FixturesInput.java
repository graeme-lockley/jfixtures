package za.co.no9.jfixture;

import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public final class FixturesInput implements Closeable {
    private final InputStream inputStream;
    private final Iterable<Object> fixtureDocuments;

    private FixturesInput(InputStream inputStream) {
        Yaml yaml = new Yaml();

        this.inputStream = inputStream;
        this.fixtureDocuments = yaml.loadAll(inputStream);
    }

    public static FixturesInput fromString(String fixtureContent) {
        InputStream inputStream = new ByteArrayInputStream(fixtureContent.getBytes());
        return new FixturesInput(inputStream);
    }

    public static FixturesInput fromResources(String resourceName) throws IOException {
        ClassLoader loader = FixturesInput.class.getClassLoader();
        InputStream inputStream = loader.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IOException("The resource " + resourceName + " could not be accessed.");
        }
        return new FixturesInput(inputStream);
    }

    public static FixturesInput fromLocation(String locationName) throws IOException {
        if (locationName.startsWith("string:")) {
            return fromString(locationName.substring(7));
        } else if (locationName.startsWith("resource:")) {
            return fromResources(locationName.substring(9));
        } else {
            throw new IOException("Resource has unknown prefix: " + locationName);
        }
    }

    public Iterable<Object> fixtureDocuments() {
        return fixtureDocuments;
    }

    public void close() throws IOException {
        inputStream.close();
    }

    public static FixturesInput none() {
        return fromString("");
    }
}
