package com.artillexstudios.axapi.nms.v1_19_R2.utils;

import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import com.destroystokyo.paper.util.maplist.IBlockDataList;
import it.unimi.dsi.fastutil.shorts.Short2LongOpenHashMap;

import java.util.Arrays;

public class IBlockDataListCopier {
    private static final FastFieldAccessor map = FastFieldAccessor.forClassField(IBlockDataList.class, "map");
    private static final FastFieldAccessor byIndex = FastFieldAccessor.forClassField(IBlockDataList.class, "byIndex");
    private static final FastFieldAccessor size = FastFieldAccessor.forClassField(IBlockDataList.class, "size");

    public static IBlockDataList copy(IBlockDataList list) {
        IBlockDataList newList = ClassUtils.INSTANCE.newInstance(IBlockDataList.class);
        map.set(newList, ((Short2LongOpenHashMap) map.get(list)).clone());
        long[] index = byIndex.get(list);
        byIndex.set(newList, Arrays.copyOf(index, index.length));
        size.setInt(newList, size.getInt(list));

        return newList;
    }
}
