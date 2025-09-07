package com.artillexstudios.axapi.reflection;

import com.artillexstudios.axapi.reflection.accessor.AccessorType;
import com.artillexstudios.axapi.utils.UncheckedUtils;
import com.artillexstudios.axapi.utils.logging.LogUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface FieldAccessor {

    static Builder builder() {
        return new Builder();
    }

    void set(Object instance, Object argument);

    void setVolatile(Object instance, Object argument);

    Object get(Object instance);

    default <T> T get(Object instance, Class<T> clazz) {
        try {
            return clazz.cast(this.get(instance));
        } catch (ClassCastException exception) {
            LogUtils.error("Failed to get field due to mismatching type!", exception);
            throw exception;
        }
    }

    default <T> T getUnchecked(Object instance) {
        return UncheckedUtils.unsafeCast(this.get(instance));
    }

    Object getVolatile(Object instance);

    default <T> T getVolatile(Object instance, Class<T> clazz) {
        return clazz.cast(this.getVolatile(instance));
    }

    default <T> T getVolatileUnchecked(Object instance) {
        return UncheckedUtils.unsafeCast(this.getVolatile(instance));
    }

    class Builder {
        private AccessorType type = AccessorType.FIELD;
        private Class<?> clazz = null;
        private Field field = null;
        private String fieldName = null;
        private Class<?> fieldType = null;
        private Integer fieldIndex = null;
        private boolean silent = false;

        public Builder methodHandle() {
            this.type = AccessorType.METHOD_HANDLE;
            return this;
        }

        public Builder varHandle() {
            this.type = AccessorType.VAR_HANDLE;
            return this;
        }

        public Builder reflect() {
            this.type = AccessorType.FIELD;
            return this;
        }

        public Builder silent() {
            this.silent = true;
            return this;
        }

        public Builder withClass(Class<?> clazz) {
            this.clazz = clazz;
            this.tryFetchField();
            return this;
        }

        public Builder withClass(String clazz) {
            this.clazz = ClassUtils.INSTANCE.getClassOrNull(clazz);
            this.tryFetchField();
            return this;
        }

        public Builder withField(Field field) {
            this.field = field;
            return this;
        }

        public Builder withField(String field) {
            this.fieldName = field;
            this.tryFetchField();
            return this;
        }

        public Builder withFieldType(Class<?> type, int index) {
            this.fieldType = type;
            this.fieldIndex = index;
            this.tryFetchField();
            return this;
        }

        private void tryFetchField() {
            if (this.clazz == null) {
                return;
            }

            if (this.fieldName != null) {
                this.fetchByName();
            } else if (this.fieldType != null) {
                this.fetchByType();
            }

            if (this.field == null) {
                return;
            }

            this.field.setAccessible(true);
            this.fieldType = null;
            this.fieldName = null;
            this.fieldIndex = null;
        }

        private void fetchByName() {
            try {
                this.field = this.clazz.getDeclaredField(this.fieldName);
            } catch (NoSuchFieldException exception) {
                if (this.silent) {
                    return;
                }

                LogUtils.error("Failed to find field {} of class {}! Fields of class: {}", this.fieldName, this.clazz.getName(), String.join(", ", Arrays.stream(this.clazz.getDeclaredFields()).map(Field::getName).toList()), exception);
                throw new RuntimeException(exception);
            }
        }

        private void fetchByType() {
            Field[] fields = Arrays.stream(this.clazz.getDeclaredFields())
                    .filter(field -> field.getType().equals(this.fieldType))
                    .toArray(Field[]::new);

            if (this.fieldIndex >= fields.length) {
                LogUtils.error("Failed to find field with type {} and index {} of class {}! Fields of class: {}", this.fieldType, this.fieldIndex, this.clazz.getName(), String.join(", ", Arrays.stream(this.clazz.getDeclaredFields()).map(Field::getName).toList()));
                return;
            }

            this.field = fields[this.fieldIndex];
        }

        public FieldAccessor build() {
            if (this.field == null) {
                return null;
            }

            try {
                return this.type.createNew(this.field);
            } catch (IllegalAccessException exception) {
                if (this.silent) {
                    return null;
                }

                throw new RuntimeException(exception);
            }
        }
    }
}
