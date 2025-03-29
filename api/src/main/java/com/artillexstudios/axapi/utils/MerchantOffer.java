package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.items.WrappedItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public final class MerchantOffer {
    private ItemStack item1;
    private Optional<ItemStack> item2;
    private ItemStack output;
    private int uses;
    private int maxUses;
    private int xp;
    private int price;
    private float priceMultiplier;
    private int demand;

    public MerchantOffer(ItemStack item1, Optional<ItemStack> item2, ItemStack output, int uses, int maxUses, int xp, int price, float priceMultiplier, int demand) {
        this.item1 = item1;
        this.item2 = item2;
        this.output = output;
        this.uses = uses;
        this.maxUses = maxUses;
        this.xp = xp;
        this.price = price;
        this.priceMultiplier = priceMultiplier;
        this.demand = demand;
    }

    public ItemStack item1() {
        return this.item1;
    }

    public void setItem1(ItemStack item1) {
        this.item1 = item1;
    }

    public Optional<ItemStack> item2() {
        return this.item2;
    }

    public void setItem2(Optional<ItemStack> item2) {
        this.item2 = item2;
    }

    public ItemStack output() {
        return this.output;
    }

    public void setOutput(ItemStack output) {
        this.output = output;
    }

    public int uses() {
        return this.uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public int maxUses() {
        return this.maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public int xp() {
        return this.xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int price() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float priceMultiplier() {
        return this.priceMultiplier;
    }

    public void setPriceMultiplier(float priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public int demand() {
        return this.demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }
}
