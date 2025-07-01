package com.artillexstudios.axapi.utils.mutable;

public final class MutableInteger implements Mutable<Integer> {
    private int value;

    public MutableInteger() {

    }

    public MutableInteger(Integer value) {
        this.value = value;
    }

    public MutableInteger(int value) {
        this.value = value;
    }

    @Override
    public Integer get() {
        return this.value;
    }

    public void set(int value) {
        this.value = value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
    }

    public int intValue() {
        return this.value;
    }

    public void increment(int amount) {
        this.value += amount;
    }

    public void increment() {
        this.value++;
    }

    public int incrementAndGet() {
        this.value++;
        return this.value;
    }

    public int getAndIncrement() {
        int prev = this.value;
        this.value++;
        return prev;
    }

    public void decrement(int amount) {
        this.value -= amount;
    }

    public void decrement() {
        this.value--;
    }

    public int decrementAndGet() {
        this.value--;
        return this.value;
    }

    public int getAndDecrement() {
        int prev = this.value;
        this.value--;
        return prev;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MutableInteger that)) {
            return false;
        }

        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.value);
    }
}
