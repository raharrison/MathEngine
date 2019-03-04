package uk.co.ryanharrison.mathengine.parser;

public interface Parser<T, R> {
    R parse(T t);
}
