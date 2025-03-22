package com.artillexstudios.axapi.nms.v1_21_R1.utils;

import ca.spottedleaf.moonrise.common.list.IBlockDataList;
import com.artillexstudios.axapi.reflection.ClassUtils;
import com.artillexstudios.axapi.reflection.FastFieldAccessor;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2LongOpenHashMap;

import java.util.Arrays;
import java.util.Map;

public class IBlockDataListCopier {
    private static final FastFieldAccessor map = FastFieldAccessor.forClassField(IBlockDataList.class, "map");
    private static final FastFieldAccessor byIndex = FastFieldAccessor.forClassField(IBlockDataList.class, "byIndex");
    private static final FastFieldAccessor size = FastFieldAccessor.forClassField(IBlockDataList.class, "size");

    public static IBlockDataList copy(IBlockDataList list) {
        IBlockDataList newList = ClassUtils.INSTANCE.newInstance(IBlockDataList.class);
        Map mapField = map.get(list);
        map.set(newList, mapField instanceof Short2LongOpenHashMap ? ((Short2LongOpenHashMap) mapField) : ((Int2IntOpenHashMap) mapField).clone());
        long[] index = byIndex.get(list);
        byIndex.set(newList, Arrays.copyOf(index, index.length));
        size.setInt(newList, size.getInt(list));

        return newList;
    }
}
