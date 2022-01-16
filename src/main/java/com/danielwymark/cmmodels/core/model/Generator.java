package com.danielwymark.cmmodels.core.model;

import java.util.stream.Stream;

public interface Generator<T> {
    Stream<T> generate();
}
