package com.no9.utils;

public interface Consumer<T> {
    <E extends Throwable> void accept(T element) throws E;
}
