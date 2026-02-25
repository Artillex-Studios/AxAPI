package com.artillexstudios.axapi.dialog.action;

import net.kyori.adventure.text.event.ClickEvent;

public record StaticAction(ClickEvent clickEvent) implements Action {
}
