package com.artillexstudios.axapi.nms.v1_21_R3.items.nbt;

import com.artillexstudios.axapi.reflection.FieldAccessor;
import com.artillexstudios.shared.axapi.nbt.Tag;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.TagType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class CompoundTag implements com.artillexstudios.shared.axapi.nbt.CompoundTag {
    private static final FieldAccessor accessor = FieldAccessor.builder()
            .withClass(net.minecraft.nbt.CompoundTag.class)
            .withField("x")
            .build();
    private final net.minecraft.nbt.CompoundTag parent;
    private final Map<String, net.minecraft.nbt.Tag> tags;

    public CompoundTag(net.minecraft.nbt.CompoundTag tag) {
        this.parent = tag;
        this.tags = accessor.getUnchecked(tag);
    }

    @Override
    public void put(String key, Tag tag) {
        this.parent.put(key, tag.getParent() instanceof net.minecraft.nbt.CompoundTag compoundTag ? compoundTag : (net.minecraft.nbt.ListTag) tag.getParent());
    }

    @Override
    public void putByte(String key, byte value) {
        this.parent.putByte(key, value);
    }

    @Override
    public void putShort(String key, short value) {
        this.parent.putShort(key, value);
    }

    @Override
    public void putInt(String key, int value) {
        this.parent.putInt(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        this.parent.putLong(key, value);
    }

    @Override
    public void putUUID(String key, UUID value) {
        if (this.parent.contains(key + "Most") && this.parent.contains(key + "Least")) {
            this.parent.remove(key + "Most");
            this.parent.remove(key + "Least");
        }

        this.putIntArray(key, uuidToIntArray(value));
    }

    @Override
    public UUID getUUID(String key) {
        return this.contains(key + "Most") && this.contains(key + "Least") ? new UUID(this.getLong(key + "Most"), this.getLong(key + "Least")) : this.contains(key) ? uuidFromIntArray(this.getIntArray(key)) : null;
    }

    @Override
    public boolean containsUUID(String key) {
        return (this.contains(key + "Least") && this.contains(key + "Most")) || this.contains(key);
    }

    @Override
    public void putFloat(String key, float value) {
        this.parent.putFloat(key, value);
    }

    @Override
    public void putDouble(String key, double value) {
        this.parent.putDouble(key, value);
    }

    @Override
    public void putString(String key, String value) {
        this.parent.putString(key, value);
    }

    @Override
    public void putByteArray(String key, byte[] value) {
        this.parent.putByteArray(key, value);
    }

    @Override
    public void putByteArray(String key, List<Byte> value) {
        byte[] bytes = new byte[value.size()];
        int i = 0;
        for (Byte b : value) {
            bytes[i] = b;
            i++;
        }

        this.parent.putByteArray(key, bytes);
    }

    @Override
    public void putIntArray(String key, int[] value) {
        this.parent.putIntArray(key, value);
    }

    @Override
    public void putIntArray(String key, List<Integer> value) {
        int[] ints = new int[value.size()];
        int i = 0;
        for (Integer integer : value) {
            ints[i] = integer;
            i++;
        }

        this.parent.putIntArray(key, ints);
    }

    @Override
    public void putLongArray(String key, long[] value) {
        this.parent.putLongArray(key, value);
    }

    @Override
    public void putLongArray(String key, List<Long> value) {
        long[] longs = new long[value.size()];
        int i = 0;
        for (Long l : value) {
            longs[i] = l;
            i++;
        }

        this.parent.putLongArray(key, longs);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        this.parent.putBoolean(key, value);
    }

    @Override
    public boolean contains(String key) {
        return this.parent.contains(key);
    }

    @Override
    public Byte getByte(String key) {
        try {
            if (this.parent.contains(key, 99)) {
                return ((NumericTag) this.tags.get(key)).getAsByte();
            }
        } catch (ClassCastException ignored) {

        }

        return null;
    }

    @Override
    public Short getShort(String key) {
        try {
            if (this.parent.contains(key, 99)) {
                return ((NumericTag) this.tags.get(key)).getAsShort();
            }
        } catch (ClassCastException ignored) {

        }

        return null;
    }

    @Override
    public Integer getInt(String key) {
        try {
            if (this.parent.contains(key, 99)) {
                return ((NumericTag) this.tags.get(key)).getAsInt();
            }
        } catch (ClassCastException ignored) {

        }

        return null;
    }

    @Override
    public Long getLong(String key) {
        try {
            if (this.parent.contains(key, 99)) {
                return ((NumericTag) this.tags.get(key)).getAsLong();
            }
        } catch (ClassCastException ignored) {

        }

        return null;
    }

    @Override
    public Float getFloat(String key) {
        try {
            if (this.parent.contains(key, 99)) {
                return ((NumericTag) this.tags.get(key)).getAsFloat();
            }
        } catch (ClassCastException ignored) {

        }

        return null;
    }

    @Override
    public Double getDouble(String key) {
        try {
            if (this.parent.contains(key, 99)) {
                return ((NumericTag) this.tags.get(key)).getAsDouble();
            }
        } catch (ClassCastException ignored) {

        }

        return null;
    }

    @Override
    public String getString(String key) {
        try {
            if (this.parent.contains(key, 8)) {
                return this.tags.get(key).getAsString();
            }
        } catch (ClassCastException ignored) {

        }

        return null;
    }

    @Override
    public byte[] getByteArray(String key) {
        try {
            if (this.parent.contains(key, 7)) {
                return ((ByteArrayTag) this.tags.get(key)).getAsByteArray();
            }
        } catch (ClassCastException var3) {
            throw new ReportedException(this.createReport(key, ByteArrayTag.TYPE, var3));
        }

        return null;
    }

    @Override
    public int[] getIntArray(String key) {
        try {
            if (this.parent.contains(key, 11)) {
                return ((IntArrayTag) this.tags.get(key)).getAsIntArray();
            }
        } catch (ClassCastException var3) {
            throw new ReportedException(this.createReport(key, IntArrayTag.TYPE, var3));
        }

        return null;
    }

    @Override
    public long[] getLongArray(String key) {
        try {
            if (this.parent.contains(key, 12)) {
                return ((LongArrayTag) this.tags.get(key)).getAsLongArray();
            }
        } catch (ClassCastException var3) {
            throw new ReportedException(this.createReport(key, LongArrayTag.TYPE, var3));
        }

        return null;
    }

    @Override
    public CompoundTag getCompound(String key) {
        try {
            if (this.parent.contains(key, 10)) {
                return new CompoundTag((net.minecraft.nbt.CompoundTag) this.tags.get(key));
            }
        } catch (ClassCastException var3) {
            throw new ReportedException(this.createReport(key, net.minecraft.nbt.CompoundTag.TYPE, var3));
        }

        return null;
    }

    @Override
    public com.artillexstudios.shared.axapi.nbt.ListTag getList(String key) {
        try {
            if (this.parent.getTagType(key) == 9) {
                net.minecraft.nbt.ListTag listTag = (net.minecraft.nbt.ListTag)this.tags.get(key);
                if (!listTag.isEmpty() && listTag.getElementType() != 10) {
                    return null;
                }

                return new ListTag(listTag);
            }
        } catch (ClassCastException var4) {
            throw new ReportedException(this.createReport(key, net.minecraft.nbt.ListTag.TYPE, var4));
        }

        return null;
    }

    @Override
    public Boolean getBoolean(String key) {
        Byte value = this.getByte(key);
        return value == null ? null : value != 0;
    }

    @Override
    public void remove(String key) {
        this.parent.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return this.parent.isEmpty();
    }

    @Override
    public Set<String> getAllKeys() {
        return this.parent.getAllKeys();
    }

    @Override
    public net.minecraft.nbt.CompoundTag getParent() {
        return this.parent;
    }

    public static UUID uuidFromIntArray(int[] array) {
        return new UUID((long) array[0] << 32 | (long) array[1] & 4294967295L, (long) array[2] << 32 | (long) array[3] & 4294967295L);
    }

    public static int[] uuidToIntArray(UUID uuid) {
        long l = uuid.getMostSignificantBits();
        long m = uuid.getLeastSignificantBits();
        return leastMostToIntArray(l, m);
    }

    private static int[] leastMostToIntArray(long uuidMost, long uuidLeast) {
        return new int[]{(int) (uuidMost >> 32), (int) uuidMost, (int) (uuidLeast >> 32), (int) uuidLeast};
    }

    private CrashReport createReport(String tagName, TagType<?> type, ClassCastException exception) {
        CrashReport crashReport = CrashReport.forThrowable(exception, "Reading NBT data");
        CrashReportCategory crashReportCategory = crashReport.addCategory("Corrupt NBT tag", 1);
        crashReportCategory.setDetail("Tag type found", () -> this.tags.get(tagName).getType().getName());
        Objects.requireNonNull(type);
        crashReportCategory.setDetail("Tag type expected", type::getName);
        crashReportCategory.setDetail("Tag name", tagName);
        return crashReport;
    }
}
