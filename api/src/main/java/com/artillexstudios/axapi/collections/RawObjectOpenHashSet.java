package com.artillexstudios.axapi.collections;

import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectSet;

public class RawObjectOpenHashSet<K> extends ObjectArraySet<K> {
    private static final FastFieldAccessor accessor = FastFieldAccessor.forClassField("it.unimi.dsi.fastutil.objects.ObjectCollections$SynchronizedCollection", "collection");

    public RawObjectOpenHashSet() {
        super();
    }

    public RawObjectOpenHashSet(final int capacity) {
        super(capacity);
    }

    public static <E> Object[] rawSet(ObjectSet<E> set) {
        RawObjectOpenHashSet<E> rawSet = accessor.get(set);
        return rawSet.rawSet();
    }

    @Override
    public boolean remove(Object k) {
        return super.remove(k);
    }

    @Override
    public RawObjectOpenHashSet<K> clone() {
        return (RawObjectOpenHashSet<K>) super.clone();
    }

    public Object[] rawSet() {
        return this.a;
    }
}
