package com.artillexstudios.axapi.utils.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class Requests {
    private static final AtomicReference<HttpClient> client = new AtomicReference<>();
    private static final Gson gson = new Gson();

    private static void init() {
        client.getAndUpdate(prev -> prev == null ? HttpClient.newHttpClient() : prev);
    }

    public static HttpResponse<String> post(String url, Map<String, String> headers, Supplier<JsonObject> body) {
        init();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body.get())));

        headers.forEach(builder::setHeader);

        try {
            return client.get().send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static HttpResponse<String> delete(String url, Map<String, String> headers) {
        init();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE();

        headers.forEach(builder::setHeader);

        try {
            return client.get().send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static HttpResponse<String> get(String url, Map<String, String> headers) {
        init();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET();

        headers.forEach(builder::setHeader);

        try {
            return client.get().send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static CompletableFuture<HttpResponse<String>> postAsync(String url, Map<String, String> headers, Supplier<JsonObject> body) {
        init();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body.get())));

        headers.forEach(builder::setHeader);

        return client.get().sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> deleteAsync(String url, Map<String, String> headers) {
        init();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE();

        headers.forEach(builder::setHeader);

        return client.get().sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> getAsync(String url, Map<String, String> headers) {
        init();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET();

        headers.forEach(builder::setHeader);

        return client.get().sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
