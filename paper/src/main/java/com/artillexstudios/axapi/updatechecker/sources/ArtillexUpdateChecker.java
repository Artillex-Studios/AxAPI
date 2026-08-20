package com.artillexstudios.axapi.updatechecker.sources;

import com.artillexstudios.axapi.updatechecker.ArtifactVersion;
import com.artillexstudios.axapi.updatechecker.Changelog;
import com.artillexstudios.axapi.updatechecker.UpdateCheck;
import com.artillexstudios.axapi.updatechecker.UpdateCheckResult;
import com.artillexstudios.axapi.utils.http.Requests;
import com.google.gson.Gson;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ArtillexUpdateChecker implements UpdateCheckSource {
    private final String id;
    private final Gson gson = new Gson();

    public ArtillexUpdateChecker(String id) {
        this.id = id;
    }

    @Override
    public UpdateCheck check(ArtifactVersion current) {
        try {
            HttpResponse<?> response = Requests.get("https://www.artillex-studios.com/api/v1/resource/" + this.id + "/latest-version", Map.of());
            if (response.statusCode() != 200) {
                return new UpdateCheck(UpdateCheckResult.FAILED, current, List.of(), new RuntimeException("Received statuscode: " + response.statusCode()));
            }

            String body = response.body().toString();
            ArtifactVersion latest = new ArtifactVersion(body);
            List<Changelog> changelogs = new ArrayList<>();

            HttpResponse<?> changelogRequest = Requests.get("https://www.artillex-studios.com/api/v1/resource/" + this.id + "/latest-update", Map.of());
            if (changelogRequest.statusCode() != 200) {
                return new UpdateCheck(UpdateCheckResult.FAILED, current, List.of(), new RuntimeException("Received statuscode: " + changelogRequest.statusCode()));
            }

            String changelogBody = changelogRequest.body().toString();
            changelogs.add(new Changelog(latest, changelogBody));
            return new UpdateCheck(latest.version() > current.version() ? UpdateCheckResult.UPDATE_AVAILABLE : latest.version() == current.version() ? UpdateCheckResult.UP_TO_DATE : UpdateCheckResult.DEV_VERSION, latest, changelogs);
        } catch (Exception e) {
            return new UpdateCheck(UpdateCheckResult.FAILED, current, List.of(), e);
        }
    }
}
