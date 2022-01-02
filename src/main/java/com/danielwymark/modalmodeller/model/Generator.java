package com.danielwymark.modalmodeller.model;

import java.util.stream.Stream;

public interface Generator<T> {
    Stream<T> generate();
}
