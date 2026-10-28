package com.artillexstudios.axapi.dialog.action;

import com.artillexstudios.axapi.items.nbt.CompoundTag;
import com.artillexstudios.axapi.utils.Version;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record StaticAction(ClickEvent clickEvent) implements Action {
    private static final Map<ClickEvent.Action, String> names;
    static {
        Map<ClickEvent.Action, String> loadingNames = new HashMap<>();
        loadingNames.put(ClickEvent.Action.COPY_TO_CLIPBOARD, "value");
        loadingNames.put(ClickEvent.Action.OPEN_FILE, Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_5) ? "path" : "value");
        loadingNames.put(ClickEvent.Action.OPEN_URL, Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_5) ? "url" : "value");
        loadingNames.put(ClickEvent.Action.RUN_COMMAND, Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_5) ? "command" : "value");
        loadingNames.put(ClickEvent.Action.SUGGEST_COMMAND, Version.getServerVersion().isNewerThanOrEqualTo(Version.v1_21_5) ? "command" : "value");
        try {
            loadingNames.put(ClickEvent.Action.SHOW_DIALOG, "dialog");
        } catch (Exception exception) {
            // On older versions this does not exist
        }
        names = Collections.unmodifiableMap(loadingNames);
    }

    @Override
    public String getType() {
        return this.clickEvent.action().toString();
    }

    @Override
    public void encode(CompoundTag tag) {
        // I have no clue if this is correct, this is not easy to figure out
        LogUtils.debug("Action name: {}, asString: {}", this.clickEvent.action().name(), this.clickEvent.toString());
        tag.putString(names.get(this.clickEvent.action()), ((ClickEvent.Payload.Text) this.clickEvent.payload()).value());
    }
}
