package com.artillexstudios.axapi.utils.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class Requests {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static CompletableFuture<HttpResponse<String>> post(String url, Map<String, String> headers, Supplier<JsonObject> body) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body.get())));

        headers.forEach(builder::setHeader);

        return client.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> delete(String url, Map<String, String> headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE();

        headers.forEach(builder::setHeader);

        return client.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public static CompletableFuture<HttpResponse<String>> get(String url, Map<String, String> headers) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET();

        headers.forEach(builder::setHeader);

        return client.sendAsync(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
