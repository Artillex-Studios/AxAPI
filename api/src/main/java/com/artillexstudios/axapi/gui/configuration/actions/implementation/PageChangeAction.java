package com.artillexstudios.axapi.gui.configuration.actions.implementation;

import com.artillexstudios.axapi.context.HashMapContext;
import com.artillexstudios.axapi.gui.configuration.actions.Action;
import com.artillexstudios.axapi.gui.inventory.Gui;
import com.artillexstudios.axapi.gui.inventory.GuiKeys;
import com.artillexstudios.axapi.gui.inventory.implementation.PaginatedGui;
import com.artillexstudios.axapi.utils.NumberUtils;
import com.artillexstudios.axapi.utils.featureflags.FeatureFlags;
import com.artillexstudios.axapi.utils.logging.LogUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class PageChangeAction extends Action<PageChangeAction.Direction> {

    public PageChangeAction(String data) {
        super(data);
    }

    @Override
    public PageChangeAction.Direction transform(String data) {
        return PageChangeAction.Direction.fetch(data);
    }

    @Override
    public void run(Player player, HashMapContext context) {
        Gui gui = context.get(GuiKeys.GUI);
        if (!(gui instanceof PaginatedGui paginatedGui)) {
            if (FeatureFlags.DEBUG.get()) {
                LogUtils.debug("Gui is not a paginated gui! Gui: {}", gui);
            }
            return;
        }

        if (this.value().page == -1) {
            if (!paginatedGui.hasPreviousPage()) {
                if (FeatureFlags.DEBUG.get()) {
                    LogUtils.debug("Gui has no previous page!");
                }
                return;
            }

            paginatedGui.page(paginatedGui.page() - 1);
        } else if (this.value().page == -2) {
            if (!paginatedGui.hasNextPage()) {
                if (FeatureFlags.DEBUG.get()) {
                    LogUtils.debug("Gui has no next page!");
                }
                return;
            }

            paginatedGui.page(paginatedGui.page() + 1);
        } else {
            if (!paginatedGui.hasPage(this.value().page)) {
                if (FeatureFlags.DEBUG.get()) {
                    LogUtils.debug("Gui has no page with index {}!", this.value().page);
                }
                return;
            }

            paginatedGui.page(this.value().page);
        }
        paginatedGui.open();
    }

    public record Direction(int page, String... aliases) {
        private static final List<Direction> directions = new ArrayList<>();
        public static final Direction BACKWARDS = register(new Direction(-1, "back", "backwards", "prev", "previous"));
        public static final Direction FORWARDS = register(new Direction(-2, "forwards", "forward", "next"));

        public static Direction register(Direction direction) {
            directions.add(direction);
            return direction;
        }

        public static Direction fetch(String directionName) {
            if (NumberUtils.isInt(directionName)) {
                return new Direction(Integer.parseInt(directionName));
            }

            for (Direction direction : directions) {
                if (ArrayUtils.contains(direction.aliases, directionName)) {
                    return direction;
                }
            }

            return null;
        }
    }
}
