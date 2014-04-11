package za.co.no9.util.function;

public final class ConsumerUtils {
    private ConsumerUtils() {
    }

    public static <T> Consumer<T> andThen(final Consumer<T> and, final Consumer<T> then) {
        return new Consumer<T>() {
            @Override
            public void accept(T element) {
                and.accept(element);
                then.accept(element);
            }
        };
    }
}
