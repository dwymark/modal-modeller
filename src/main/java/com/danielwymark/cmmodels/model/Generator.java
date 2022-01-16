package com.danielwymark.cmmodels.model;

import java.util.stream.Stream;

public interface Generator<T> {
    Stream<T> generate();
}
