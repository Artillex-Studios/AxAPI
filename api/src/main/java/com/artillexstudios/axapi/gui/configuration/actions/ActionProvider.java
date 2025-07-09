package com.artillexstudios.axapi.gui.configuration.actions;

public interface ActionProvider<T extends Action> {

    T provide(String data);
}
