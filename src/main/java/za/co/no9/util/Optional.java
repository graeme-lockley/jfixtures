package za.co.no9.util;

import za.co.no9.util.function.Consumer;

public class Optional<T> {
    private T element;

    private Optional(T element) {
        this.element = element;
    }

    public static <T> Optional<T> of(T element) {
        if (element == null) {
            throw new NullPointerException("Attempted to create an optional with a null element.");
        }
        return new Optional<T>(element);
    }

    public static <T> Optional<T> empty() {
        return new Optional<T>(null);
    }

    public boolean isPresent() {
        return element != null;
    }

    public boolean isNotPresent() {
        return element == null;
    }

    public T get() {
        if (element == null) {
            throw new NullPointerException("Attempted get on null optional element.");
        }
        return element;
    }

    public T orElse(T elseElement) {
        return element == null ? elseElement : element;
    }

    public void ifPresent(Consumer<T> consumer) {
        if (element != null) {
            consumer.accept(element);
        }
    }
}
