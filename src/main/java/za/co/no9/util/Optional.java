package za.co.no9.util;

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

    public static <T> Optional<T> none() {
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

    public <E extends Throwable> void ifPresent(Consumer<T> consumer) throws E {
        if (element != null) {
            consumer.<E>accept(element);
        }
    }
}
