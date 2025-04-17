package com.artillexstudios.axapi.database;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseQuery<T> {
    private final DatabaseHandler handler;
    private final ResultHandler<T> resultHandler;
    private final String sql;

    public DatabaseQuery(DatabaseHandler handler, ResultHandler<T> resultHandler, String sql) {
        this.handler = handler;
        this.resultHandler = resultHandler;
        this.sql = sql;
    }

    public RunnableQuery<T> create() {
        return new RunnableQuery<>(handler::connection, this.resultHandler, this.sql);
    }

    public AsyncRunnableQuery<T> createAsync() {
        return new AsyncRunnableQuery<>(Executors::newVirtualThreadPerTaskExecutor, this.create());
    }

    public AsyncRunnableQuery<T> createAsync(ExecutorService service) {
        return new AsyncRunnableQuery<>(() -> service, this.create());
    }

    public AsyncRunnableQuery<T> createAsync(Executor executor) {
        return new AsyncRunnableQuery<>(() -> executor, this.create());
    }
}
