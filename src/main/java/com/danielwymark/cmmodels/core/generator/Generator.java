package com.danielwymark.cmmodels.core.generator;

import java.util.stream.Stream;

public interface Generator<T> {
    Stream<T> generate();
}
