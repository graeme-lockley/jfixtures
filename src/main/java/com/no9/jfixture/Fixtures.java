package com.no9.jfixture;

import com.no9.utils.Optional;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Fixtures implements Closeable {
    private Optional<FixturesInput> input;
    private FixtureHandler[] handlers;

    private Fixtures(FixturesInput input, FixtureHandler... handlers) throws IOException {
        this.input = Optional.of(input);
        this.handlers = handlers;

        addHandler(new NewHandlerHandler());
        addHandler(new IncludeHandler());
    }

    public static Fixtures load(FixturesInput input, FixtureHandler... handlers) throws IOException {
        return new Fixtures(input, handlers);
    }

    public static Fixtures load(FixturesInput input) throws IOException {
        return load(input, new FixtureHandler[0]);
    }

    public static Fixtures process(FixturesInput input) throws IOException, FixtureException {
        Fixtures fixtures = load(input);
        fixtures.processFixtures();
        return fixtures;
    }

    public void processFixtures(FixturesInput input) throws FixtureException {
        Optional<FixturesInput> oldInput = this.input;

        try {
            this.input = Optional.of(input);
            processFixtures();
        } finally {
            this.input = oldInput;
        }
    }

    public void processFixtures() throws FixtureException {
        for (Object fixtureDocument : input.orElse(FixturesInput.none()).fixtureDocuments()) {
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

            if (handler instanceof BasicFixtureHandler) {
                ((BasicFixtureHandler) handler).process(fixtureInput);
            } else {
                ((ExtendedFixtureHandler) handler).process(this, fixtureInput);
            }
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
        input.orElse(FixturesInput.none()).close();
    }

    public FixtureHandler[] handlers() {
        return handlers.clone();
    }

    public void addHandler(FixtureHandler fixtureHandler) {
        this.handlers = Arrays.copyOf(handlers, handlers.length + 1);
        this.handlers[handlers.length - 1] = fixtureHandler;
    }

    public <T extends FixtureHandler> Optional<T> findHandler(Class<T> classInstance) {
        for (FixtureHandler handler : handlers()) {
            if (classInstance.isInstance(handler)) {
                return Optional.of((T) handler);
            }
        }

        return Optional.none();
    }

    public <T extends FixtureHandler> T handler(Class<T> classInstance) {
        return findHandler(classInstance).get();
    }
}
