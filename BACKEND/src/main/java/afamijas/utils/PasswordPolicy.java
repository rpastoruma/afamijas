package afamijas.utils;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class PasswordPolicy {

    public static final String MSG = "Passwords must be at least 9 characters long and contain uppercase, lowercase, numbers, and at least one of these symbols: % _ $ - ! *";

    private static final String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789%_$-!*";
    private static final String ALLOWED_UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALLOWED_LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String ALLOWED_DIGITS = "0123456789";
    private static final String ALLOWED_SYMBOLS = "%_$-!*";

    private static final int MIN_LENGTH = 9;
    private static final int MAX_LENGTH = 256; // DB LIMIT

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static boolean check(String password) {
        if (password == null || password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            return false;
        }

        // Validar que solo contenga caracteres permitidos
        if (!password.matches("^[A-Za-z0-9%_\\$\\-!\\*]+$")) {
            return false;
        }

        // Validar presencia de al menos una mayúscula, minúscula, número y símbolo
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*[0-9].*")) return false;
        if (!password.matches(".*[%_\\$\\-!\\*].*")) return false;

        return true;
    }

    public static String generate() {
        StringBuilder sb = new StringBuilder();
        sb.append(randomChars(ALLOWED_UPPER_CHARS, 3));
        sb.append(randomChars(ALLOWED_LOWER_CHARS, 3));
        sb.append(randomChars(ALLOWED_SYMBOLS, 1));
        sb.append(randomChars(ALLOWED_DIGITS, 2));

        return shuffle(sb.toString());
    }

    public static String generateForJDBC() {
        StringBuilder sb = new StringBuilder();
        sb.append(randomChars(ALLOWED_UPPER_CHARS, 3));
        sb.append(randomChars(ALLOWED_LOWER_CHARS, 3));
        sb.append("_");
        sb.append(randomChars(ALLOWED_DIGITS, 2));

        return shuffle(sb.toString());
    }

    private static String randomChars(String source, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(source.charAt(SECURE_RANDOM.nextInt(source.length())));
        }
        return result.toString();
    }

    private static String shuffle(String input) {
        List<Character> chars = input.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(chars, SECURE_RANDOM);
        StringBuilder shuffled = new StringBuilder();
        for (char c : chars) {
            shuffled.append(c);
        }
        return shuffled.toString();
    }
}
