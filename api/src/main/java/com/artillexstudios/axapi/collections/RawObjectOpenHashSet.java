package com.artillexstudios.axapi.collections;

import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;

public class RawObjectOpenHashSet<K> extends ObjectOpenHashSet<K> {
    private static final FastFieldAccessor accessor = FastFieldAccessor.forClassField("it.unimi.dsi.fastutil.objects.ObjectCollections$SynchronizedCollection", "collection");
    public RawObjectOpenHashSet() {
        super();
    }

    public RawObjectOpenHashSet(final int capacity) {
        super(capacity);
    }

    public RawObjectOpenHashSet(final int capacity, final float loadFactor) {
        super(capacity, loadFactor);
    }

    @Override
    public RawObjectOpenHashSet<K> clone() {
        return (RawObjectOpenHashSet<K>) super.clone();
    }

    public K[] rawSet() {
        return this.key;
    }

    public static <E> E[] rawSet(ObjectSet<E> set) {
        RawObjectOpenHashSet<E> rawSet = accessor.get(set);
        return rawSet.rawSet();
    }
}
