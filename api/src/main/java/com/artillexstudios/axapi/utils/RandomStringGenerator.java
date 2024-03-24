package com.artillexstudios.axapi.utils;

import java.util.concurrent.ThreadLocalRandom;

public interface RandomStringGenerator {

    static RandomStringGenerator lowercase() {
        return RandomLowercaseStringGenerator.INSTANCE;
    }

    static RandomStringGenerator uppercase() {
        return RandomUppercaseStringGenerator.INSTANCE;
    }

    String generate(int length);

    class RandomLowercaseStringGenerator implements RandomStringGenerator {
        private static final char[] characters = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        private static final RandomStringGenerator INSTANCE = new RandomLowercaseStringGenerator();

        @Override
        public String generate(int length) {
            char[] randomChars = new char[length];

            for (int i = 0; i < length; i++) {
                randomChars[i] = characters[ThreadLocalRandom.current().nextInt(characters.length)];
            }

            return new String(randomChars);
        }
    }

    class RandomUppercaseStringGenerator implements RandomStringGenerator {
        private static final char[] characters = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        private static final RandomStringGenerator INSTANCE = new RandomUppercaseStringGenerator();

        public static RandomStringGenerator getInstance() {
            return INSTANCE;
        }

        @Override
        public String generate(int length) {
            char[] randomChars = new char[length];

            for (int i = 0; i < length; i++) {
                randomChars[i] = characters[ThreadLocalRandom.current().nextInt(characters.length)];
            }

            return new String(randomChars);
        }
    }
}
