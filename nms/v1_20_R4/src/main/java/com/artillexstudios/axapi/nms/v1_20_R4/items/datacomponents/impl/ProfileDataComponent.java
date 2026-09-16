package com.artillexstudios.axapi.nms.v1_20_R4.items.datacomponents.impl;

import com.artillexstudios.axapi.items.component.type.ProfileProperties;
import com.artillexstudios.axapi.utils.PlayerSkin;
import com.artillexstudios.axapi.utils.ResolvableProfile;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import java.util.Map;

public final class ProfileDataComponent implements DataComponentHandler<ResolvableProfile, net.minecraft.world.item.component.ResolvableProfile> {

    @Override
    public net.minecraft.world.item.component.ResolvableProfile toNMS(ResolvableProfile from) {
        GameProfile gameProfile = new GameProfile(from.getPartialProfile().properties().uuid(), from.getPartialProfile().properties().name());
        for (Map.Entry<String, ProfileProperties.Property> entry : from.getPartialProfile().properties().properties().entries()) {
            var property = entry.getValue();
            gameProfile.getProperties().put(entry.getKey(), new Property(property.name(), property.value(), property.signature()));
        }

        return new net.minecraft.world.item.component.ResolvableProfile(gameProfile);
    }

    @Override
    public ResolvableProfile fromNMS(net.minecraft.world.item.component.ResolvableProfile data) {
        ProfileProperties profileProperties = new ProfileProperties(data.gameProfile().getId(), data.name().orElse(""));
        data.gameProfile().getProperties()
                .forEach((k, v) -> profileProperties.put(k, new ProfileProperties.Property(v.name(), v.value(), v.signature())));
        com.artillexstudios.axapi.utils.GameProfile gameProfile = new com.artillexstudios.axapi.utils.GameProfile(data.gameProfile().getName(), data.gameProfile().getId(), profileProperties);
        return new ResolvableProfile(gameProfile, new PlayerSkin.Patch(null, null, null, null));
    }
}
