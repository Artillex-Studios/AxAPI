package com.artillexstudios.axapi.nms.v1_20_R2.items.datacomponents.impl;

import com.artillexstudios.axapi.items.component.type.ProfileProperties;
import com.artillexstudios.axapi.utils.GameProfile;
import com.artillexstudios.axapi.utils.ResolvableProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public final class ProfileDataComponent implements DataComponentHandler<ResolvableProfile> {
    
    @Override
    public void setData(ItemStack from, ResolvableProfile data) {
        CompoundTag tag = this.getOrCreateTag(from);
        CompoundTag skullOwner = new CompoundTag();
        skullOwner.putString("Name", "skull");
        CompoundTag properties = new CompoundTag();

        ListTag textures = new ListTag();
        CompoundTag val = new CompoundTag();
        val.putString("Value", data.getPartialProfile().properties().properties().get("textures").stream().findFirst().get().value());
        textures.add(val);
        properties.put("textures", textures);

        skullOwner.put("Properties", properties);
        tag.put("SkullOwner", skullOwner);
    }

    @Override
    public ResolvableProfile getData(ItemStack stack) {
        CompoundTag tag = this.getTag(stack);

        if (tag == null || !tag.contains("SkullOwner")) {
            return null;
        }

        ProfileProperties profileProperties = new ProfileProperties(UUID.randomUUID(), "skull");
        CompoundTag skullOwner = tag.getCompound("SkullOwner");


        CompoundTag propertiesTag = skullOwner.getCompound("Properties");
        ListTag listTag = propertiesTag.getList("textures", 10);
        String textures = "";
        for (Tag tag1 : listTag) {
            CompoundTag compoundTag = (CompoundTag) tag1;
            textures = compoundTag.getString("Value");
            break;
        }

        profileProperties.put("textures", new ProfileProperties.Property("textures", textures, null));
        return new ResolvableProfile(new GameProfile(null, null, profileProperties), null);
    }

    @Override
    public void removeData(ItemStack itemStack) {
        CompoundTag tag = this.getTag(itemStack);
        if (tag == null || !tag.contains("SkullOwner")) {
            return;
        }

        tag.remove("SkullOwner");
    }
}
