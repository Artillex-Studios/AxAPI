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
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SpigetUpdateCheckSource implements UpdateCheckSource {
    private final int id;
    private final Gson gson = new Gson();

    public SpigetUpdateCheckSource(int id) {
        this.id = id;
    }

    @Override
    public UpdateCheck check(ArtifactVersion current) {
        try {
            HttpResponse<?> response = Requests.get("https://api.spiget.org/v2/resources/" + this.id + " /updates?size=300&sort=-id", Map.of());
            if (response.statusCode() != 200) {
                return new UpdateCheck(UpdateCheckResult.FAILED, current, List.of(), new RuntimeException("Received statuscode: " + response.statusCode()));
            }

            String body = response.body().toString();
            List<Changelog> changelogs = new ArrayList<>();
            JsonArray obj = this.gson.fromJson(body, JsonArray.class);
            ArtifactVersion latest = new ArtifactVersion(obj.get(0).getAsJsonObject().get("title").getAsString());

            for (JsonElement jsonElement : obj) {
                JsonObject object = jsonElement.getAsJsonObject();
                ArtifactVersion version = new ArtifactVersion(object.get("title").getAsString());
                if (version.version() > current.version()) {
                    changelogs.add(new Changelog(version, new String(Base64Coder.decode(object.get("description").getAsString()))));
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
