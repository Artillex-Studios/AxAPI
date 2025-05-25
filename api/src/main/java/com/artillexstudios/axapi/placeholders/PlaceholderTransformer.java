package com.artillexstudios.axapi.placeholders;

import java.util.function.Function;

public record PlaceholderTransformer<T, Z>(Class<T> from, Class<Z> to, Function<T, Z> function) {
}
