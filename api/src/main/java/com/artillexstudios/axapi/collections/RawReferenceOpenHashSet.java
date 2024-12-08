package com.artillexstudios.axapi.collections;

import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;

public class RawReferenceOpenHashSet<K> extends ReferenceOpenHashSet<K> {
    private static final FastFieldAccessor accessor = FastFieldAccessor.forClassField("it.unimi.dsi.fastutil.objects.ObjectCollections$SynchronizedCollection", "collection");

    public RawReferenceOpenHashSet() {
        super();
    }

    public RawReferenceOpenHashSet(final int capacity) {
        super(capacity);
    }

    public static <E> Object[] rawSet(ReferenceSet<E> set) {
        RawReferenceOpenHashSet<E> rawSet = accessor.get(set);
        return rawSet.rawSet();
    }

    @Override
    public boolean remove(Object k) {
        return super.remove(k);
    }

    @Override
    public RawReferenceOpenHashSet<K> clone() {
        return (RawReferenceOpenHashSet<K>) super.clone();
    }

    public Object[] rawSet() {
        return this.key;
    }
}
