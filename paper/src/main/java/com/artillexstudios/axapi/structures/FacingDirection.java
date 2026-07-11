package com.artillexstudios.axapi.structures;

public enum FacingDirection {
    NORTH {
        @Override
        public ComplexOffset getOffset(int x, int z) {
            return new ComplexOffset(-x, 0, -z);
        }
    },
    EAST {
        @Override
        public ComplexOffset getOffset(int x, int z) {
            return new ComplexOffset(-z, 0, x);
        }
    },
    SOUTH {
        @Override
        public ComplexOffset getOffset(int x, int z) {
            return new ComplexOffset(x, 0, z);
        }
    },
    WEST {
        @Override
        public ComplexOffset getOffset(int x, int z) {
            return new ComplexOffset(z, 0, -x);
        }
    };

    public abstract ComplexOffset getOffset(int x, int z);
}
