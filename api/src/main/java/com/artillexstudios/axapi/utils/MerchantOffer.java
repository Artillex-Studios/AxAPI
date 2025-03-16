package com.artillexstudios.axapi.utils;

import com.artillexstudios.axapi.items.WrappedItemStack;

import java.util.Optional;

public final class MerchantOffer {
    private WrappedItemStack item1;
    private Optional<WrappedItemStack> item2;
    private WrappedItemStack output;
    private int uses;
    private int maxUses;
    private int xp;
    private int price;
    private float priceMultiplier;
    private int demand;

    public MerchantOffer(WrappedItemStack item1, Optional<WrappedItemStack> item2, WrappedItemStack output, int uses, int maxUses, int xp, int price, float priceMultiplier, int demand) {
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

    public WrappedItemStack item1() {
        return this.item1;
    }

    public void setItem1(WrappedItemStack item1) {
        this.item1 = item1;
    }

    public Optional<WrappedItemStack> item2() {
        return this.item2;
    }

    public void setItem2(Optional<WrappedItemStack> item2) {
        this.item2 = item2;
    }

    public WrappedItemStack output() {
        return this.output;
    }

    public void setOutput(WrappedItemStack output) {
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
