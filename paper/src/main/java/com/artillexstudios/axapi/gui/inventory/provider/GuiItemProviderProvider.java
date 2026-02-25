package com.artillexstudios.axapi.gui.inventory.provider;

public interface GuiItemProviderProvider<T> {

    GuiItemProvider provide(T data);
}
