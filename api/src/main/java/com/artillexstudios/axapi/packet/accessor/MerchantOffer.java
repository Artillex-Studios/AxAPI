package com.artillexstudios.axapi.packet.accessor;

import com.artillexstudios.axapi.items.WrappedItemStack;

public final class MerchantOffer {
    private WrappedItemStack item1;
    private WrappedItemStack item2;
    private WrappedItemStack output;
    private int uses;
    private int maxUses;
    private int xp;
    private int price;
    private float priceMultiplier;
    private int demand;

    public MerchantOffer(WrappedItemStack item1, WrappedItemStack item2, WrappedItemStack output, int uses, int maxUses, int xp, int price, float priceMultiplier, int demand) {
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
        return item1;
    }

    public void setItem1(WrappedItemStack item1) {
        this.item1 = item1;
    }

    public WrappedItemStack item2() {
        return item2;
    }

    public void setItem2(WrappedItemStack item2) {
        this.item2 = item2;
    }

    public WrappedItemStack output() {
        return output;
    }

    public void setOutput(WrappedItemStack output) {
        this.output = output;
    }

    public int uses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public int maxUses() {
        return maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public int xp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int price() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float priceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(float priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public int demand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }
}
