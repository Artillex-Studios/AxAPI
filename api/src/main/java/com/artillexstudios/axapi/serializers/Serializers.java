package com.artillexstudios.axapi.serializers;

import com.artillexstudios.axapi.serializers.impl.ItemStackSerializer;
import com.artillexstudios.axapi.serializers.impl.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Serializers {
    public static final Serializer<Location> LOCATION = new LocationSerializer();

    public static final Serializer<ItemStack> ITEM_STACK = new ItemStackSerializer();
}
