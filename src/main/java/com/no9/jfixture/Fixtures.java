package com.no9.jfixture;

import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fixtures implements Closeable {
    private InputStream inputStream;
    private Iterable<Object> fixtureDocuments;
    private final Iterable<FixtureHandler> handlers;

    private Fixtures(InputStream inputStream, Iterable<FixtureHandler> handlers) throws IOException {
        Yaml yaml = new Yaml();

        this.inputStream = inputStream;
        this.fixtureDocuments = yaml.loadAll(inputStream);
        this.handlers = handlers;
    }

    public static Fixtures loadFromResources(String resourceName, Iterable<FixtureHandler> handlers) throws IOException {
        ClassLoader loader = Fixtures.class.getClassLoader();
        InputStream inputStream = loader.getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IOException("The resource " + resourceName + " could not be accessed.");
        }
        return new Fixtures(inputStream, handlers);
    }

    public static Fixtures loadFromResources(String resourceName) throws IOException {
        return loadFromResources(resourceName, new ArrayList<FixtureHandler>());
    }

    public static Fixtures fromString(String fixtureContent) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(fixtureContent.getBytes());
        return new Fixtures(inputStream, new ArrayList<FixtureHandler>());
    }

    public void processFixtures() throws FixtureException {
        for (Object fixtureDocument : fixtureDocuments) {
            processDocument(fixtureDocument);
        }

        try {
            closeHandlers();
        } catch (IOException ex) {
            throw new FixtureException(ex);
        }
    }

    protected void closeHandlers() throws IOException {
        for (FixtureHandler handler : handlers) {
            handler.close();
        }
    }

    private void processDocument(Object fixtureDocument) throws FixtureException {
        if (fixtureDocument instanceof List) {
            processSequence((List<Map<String, Object>>) fixtureDocument);
        } else {
            throw new FixtureException("The top-level of a fixture control file must be a sequence.");
        }
    }

    private void processSequence(List<Map<String, Object>> sequenceOfFixtureInputs) throws FixtureException {
        for (Map<String, Object> fixtureInput : sequenceOfFixtureInputs) {
            processFixture(fixtureInput);
        }
    }

    private void processFixture(Map<String, Object> fixtureInput) throws FixtureException {
        if (fixtureInput.size() == 1) {
            FixtureHandler handler = getHandler(fixtureInput);
            handler.process(fixtureInput);
        } else {
            throw new FixtureException("Each fixture must have a single selector.");
        }
    }

    private FixtureHandler getHandler(Map<String, Object> fixtureInput) throws FixtureException {
        for (FixtureHandler handler : handlers) {
            if (handler.canProcess(fixtureInput)) {
                return handler;
            }
        }
        throw new FixtureException("No fixture handler has been registered to support " + fixtureInput.toString());
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
            inputStream = null;
        }
    }
}
