package com.artillexstudios.axapi.nms.v1_21_R7.items.datacomponents.impl;

import com.artillexstudios.axapi.items.component.type.ProfileProperties;
import com.artillexstudios.axapi.utils.PlayerSkin;
import com.artillexstudios.axapi.utils.ResolvableProfile;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ProfileDataComponent implements DataComponentHandler<ResolvableProfile, net.minecraft.world.item.component.ResolvableProfile> {

    @Override
    public net.minecraft.world.item.component.ResolvableProfile toNMS(ResolvableProfile from) {
        Multimap<@NotNull String, @NotNull Property> propertyMap = HashMultimap.create();
        for (Map.Entry<String, ProfileProperties.Property> entry : from.getPartialProfile().properties().properties().entries()) {
            var property = entry.getValue();
            propertyMap.put(entry.getKey(), new Property(property.name(), property.value(), property.signature()));
        }

        GameProfile gameProfile = new GameProfile(from.getPartialProfile().properties().uuid(), from.getPartialProfile().properties().name(), new PropertyMap(propertyMap));
        return net.minecraft.world.item.component.ResolvableProfile.createResolved(gameProfile);
    }

    @Override
    public ResolvableProfile fromNMS(net.minecraft.world.item.component.ResolvableProfile data) {
        ProfileProperties profileProperties = new ProfileProperties(data.partialProfile().id(), data.name().orElse(""));
        data.partialProfile().properties()
                .forEach((k, v) -> profileProperties.put(k, new ProfileProperties.Property(v.name(), v.value(), v.signature())));
        com.artillexstudios.axapi.utils.GameProfile gameProfile = new com.artillexstudios.axapi.utils.GameProfile(data.partialProfile().name(), data.partialProfile().id(), profileProperties);
        return new ResolvableProfile(gameProfile, new PlayerSkin.Patch(null, null, null, null));
    }
}
