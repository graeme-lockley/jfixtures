package za.co.no9.util;

public interface Consumer<T> {
    <E extends Throwable> void accept(T element) throws E;
}
