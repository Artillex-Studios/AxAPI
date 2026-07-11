package com.artillexstudios.axapi.structures;

import com.artillexstudios.axapi.collections.RegistrationFailedException;
import com.artillexstudios.axapi.collections.Registry;

import java.util.Collection;
import java.util.Locale;

public final class StructureRegistry {
    private final Registry<String, Structure> structureRegistry = new Registry<>();

    public void register(Structure structure) throws RegistrationFailedException {
        this.structureRegistry.register(structure.id().toLowerCase(Locale.ENGLISH), structure);
    }

    public void deregister(String structureId) throws RegistrationFailedException {
        this.structureRegistry.deregister(structureId.toLowerCase(Locale.ENGLISH));
    }

    public Collection<Structure> structures() {
        return this.structureRegistry.values();
    }

    private static final class Holder {
        private static final StructureRegistry INSTANCE = new StructureRegistry();
    }

    public static StructureRegistry getInstance() {
        return Holder.INSTANCE;
    }
}
