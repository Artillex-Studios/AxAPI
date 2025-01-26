package com.artillexstudios.axapi.utils.updatechecker.sources;

import com.artillexstudios.axapi.utils.updatechecker.ArtifactVersion;
import com.artillexstudios.axapi.utils.updatechecker.Changelog;
import com.artillexstudios.axapi.utils.updatechecker.UpdateCheck;
import com.artillexstudios.axapi.utils.updatechecker.UpdateCheckResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public final class PolymartUpdateCheckSource implements UpdateCheckSource {
    private final int id;

    public PolymartUpdateCheckSource(int id) {
        this.id = id;
    }

    @Override
    public UpdateCheck check(ArtifactVersion current) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.polymart.org/v1/getResourceUpdates/?pretty_print_result=1&resource_id=" + this.id + "&start=0&limit=3000"))
                    .timeout(Duration.of(10, ChronoUnit.SECONDS))
                    .GET()
                    .build();

            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return new UpdateCheck(UpdateCheckResult.FAILED, current, List.of(), new RuntimeException("Received statuscode: " + response.statusCode()));
            }

            String body = response.body().toString();
            List<Changelog> changelogs = new ArrayList<>();
            JsonObject obj = new Gson().fromJson(body, JsonObject.class);
            obj = obj.get("response").getAsJsonObject();
            JsonArray updates = obj.get("updates").getAsJsonArray();
            ArtifactVersion latest = new ArtifactVersion(updates.get(0).getAsJsonObject().get("version").getAsString());

            for (JsonElement jsonElement : updates) {
                JsonObject object = jsonElement.getAsJsonObject();
                ArtifactVersion version = new ArtifactVersion(object.get("version").getAsString());
                if (version.version() > current.version()) {
                    changelogs.add(new Changelog(version, object.get("description").getAsString()));
                } else if (version.version() == current.version()) {
                    break;
                }
            }

            return new UpdateCheck(latest.version() > current.version() ? UpdateCheckResult.UPDATE_AVAILABLE : latest.version() == current.version() ? UpdateCheckResult.UP_TO_DATE : UpdateCheckResult.DEV_VERSION, latest, changelogs);
        } catch (Exception e) {
            return new UpdateCheck(UpdateCheckResult.FAILED, current, List.of(), e);
        }
    }
}
