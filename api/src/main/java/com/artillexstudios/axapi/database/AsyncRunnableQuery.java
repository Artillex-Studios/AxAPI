package com.artillexstudios.axapi.database;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class AsyncRunnableQuery<T> {
    private final Supplier<Executor> executorSupplier;
    private final RunnableQuery<T> query;

    public AsyncRunnableQuery(Supplier<Executor> executorSupplier, RunnableQuery<T> query) {
        this.executorSupplier = executorSupplier;
        this.query = query;
    }

    public CompletableFuture<Integer> update(Object... parameters) {
        return CompletableFuture.supplyAsync(() -> query.update(parameters), executorSupplier.get());
    }

    public CompletableFuture<T> execute(Object... parameters) {
        return CompletableFuture.supplyAsync(() -> query.execute(parameters), executorSupplier.get());
    }

    public CompletableFuture<T> query(Object... parameters) {
        return CompletableFuture.supplyAsync(() -> query.query(parameters), executorSupplier.get());
    }

    public CompletableFuture<int[]> batch(List<Object[]> batch) {
        return CompletableFuture.supplyAsync(() -> query.batch(batch), executorSupplier.get());
    }
}
