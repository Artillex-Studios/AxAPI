package com.artillexstudios.axapi.updatechecker.sources;

import com.artillexstudios.axapi.updatechecker.ArtifactVersion;
import com.artillexstudios.axapi.updatechecker.Changelog;
import com.artillexstudios.axapi.updatechecker.UpdateCheck;
import com.artillexstudios.axapi.updatechecker.UpdateCheckResult;
import com.artillexstudios.axapi.utils.http.Requests;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ModrinthUpdateCheckSource implements UpdateCheckSource {
    private final String id;
    private final Gson gson = new Gson();

    public ModrinthUpdateCheckSource(String id) {
        this.id = id;
    }

    @Override
    public UpdateCheck check(ArtifactVersion current) {
        try {
            HttpResponse<?> response = Requests.get("https://api.modrinth.com/v2/project/" + this.id + "/version", Map.of());
            if (response.statusCode() != 200) {
                return new UpdateCheck(UpdateCheckResult.FAILED, current, List.of(), new RuntimeException("Received statuscode: " + response.statusCode()));
            }

            String body = response.body().toString();
            List<Changelog> changelogs = new ArrayList<>();
            JsonArray obj = this.gson.fromJson(body, JsonArray.class);
            ArtifactVersion latest = new ArtifactVersion(obj.get(0).getAsJsonObject().get("version_number").getAsString());

            for (JsonElement jsonElement : obj) {
                JsonObject object = jsonElement.getAsJsonObject();
                ArtifactVersion version = new ArtifactVersion(object.get("version_number").getAsString());
                if (version.version() > current.version()) {
                    changelogs.add(new Changelog(version, object.get("changelog").getAsString()));
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
