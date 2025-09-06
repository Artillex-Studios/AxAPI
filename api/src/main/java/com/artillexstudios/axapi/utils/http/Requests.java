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
import java.util.function.Supplier;

public final class Requests {
    private static final class Holder {
        private static final HttpClient CLIENT = HttpClient.newHttpClient();
        private static final Gson GSON = new Gson();
    }

    private static HttpRequest createRequest(RequestType type, String url, Map<String, String> headers, Supplier<JsonObject> bodySupplier) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url));

        builder = switch (type) {
            case GET -> builder.GET();
            case POST -> {
                if (bodySupplier == null) {
                    throw new IllegalArgumentException("The body supplier of a POST request can't be null!");
                }

                JsonObject body = bodySupplier.get();
                if (body == null) {
                    throw new IllegalArgumentException("The body of a POST request can't be null!");
                }

                yield builder.POST(HttpRequest.BodyPublishers.ofString(Holder.GSON.toJson(body)));
            }
            case DELETE -> builder.DELETE();
        };

        if (headers != null) {
            headers.forEach(builder::setHeader);
        }

        return builder.build();
    }

    public static HttpResponse<String> sendTyped(RequestType type, String url, Map<String, String> headers, Supplier<JsonObject> body) {
        HttpRequest request = createRequest(type, url, headers, body);

        try {
            return Holder.CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<HttpResponse<String>> sendTypedAsync(RequestType type, String url, Map<String, String> headers, Supplier<JsonObject> body) {
        HttpRequest request = createRequest(type, url, headers, body);
        return Holder.CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> post(String url, Map<String, String> headers, Supplier<JsonObject> body) {
        return sendTyped(RequestType.POST, url, headers, body);
    }

    public static HttpResponse<String> delete(String url, Map<String, String> headers) {
        return sendTyped(RequestType.DELETE, url, headers, null);
    }

    public static HttpResponse<String> get(String url, Map<String, String> headers) {
        return sendTyped(RequestType.GET, url, headers, null);
    }

    public static CompletableFuture<HttpResponse<String>> postAsync(String url, Map<String, String> headers, Supplier<JsonObject> body) {
        return sendTypedAsync(RequestType.POST, url, headers, body);
    }

    public static CompletableFuture<HttpResponse<String>> deleteAsync(String url, Map<String, String> headers) {
        return sendTypedAsync(RequestType.DELETE, url, headers, null);
    }

    public static CompletableFuture<HttpResponse<String>> getAsync(String url, Map<String, String> headers) {
        return sendTypedAsync(RequestType.GET, url, headers, null);
    }
}
