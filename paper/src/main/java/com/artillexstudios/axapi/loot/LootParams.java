package com.artillexstudios.axapi.loot;

import org.bukkit.World;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public record LootParams(World world, Map<LootContextParam<?>, Object> params, LootContextParamSets sets) {

    public <T> T param(LootContextParam<T> parameter) {
        T obj = (T) this.params.get(parameter);
        if (obj == null) {
            throw new NoSuchElementException(parameter.key().asString());
        }

        return obj;
    }

    public static final class Builder {
        private final World world;
        private final Map<LootContextParam<?>, Object> params = new IdentityHashMap<>(10);

        public Builder(World world) {
            this.world = world;
        }

        public <T> Builder withParameter(LootContextParam<T> param, T value) {
            this.params.put(param, value);
            return this;
        }

        public <T> Builder withOptionalParameter(LootContextParam<T> param, T value) {
            if (value == null) {
                this.params.remove(param);
            } else {
                this.params.put(param, value);
            }

            return this;
        }

        public <T> T parameter(LootContextParam<T> param) {
            return (T) this.params.get(param);
        }

        public LootParams build(LootContextParamSets paramSets) {
            return new LootParams(this.world, this.params, paramSets);
        }
    }
}
