package com.artillexstudios.axapi.serializers;

import com.artillexstudios.axapi.serializers.impl.ItemArraySerializer;
import com.artillexstudios.axapi.serializers.impl.ItemStackSerializer;
import com.artillexstudios.axapi.serializers.impl.LocationSerializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Serializers {
    public static final Serializer<Location, String> LOCATION = new LocationSerializer();

    public static final Serializer<ItemStack, String> ITEM_STACK = new ItemStackSerializer();

    public static final Serializer<ItemStack[], String> ITEM_ARRAY = new ItemArraySerializer();
}
