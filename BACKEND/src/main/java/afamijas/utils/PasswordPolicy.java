package afamijas.utils;

import java.util.Random;

public class PasswordPolicy
{
    public final static String MSG = "Passwords must be at least 9 characters long and contain uppercase, lowercase, numbers, and at least one of these symbols: % _ $ - ! *";

    private final static String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789%_$-!*";

    private final static String ALLOWED_UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private final static String ALLOWED_LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";

    private final static String ALLOWED_DIGITS = "0123456789";

    private final static String ALLOWED_SYMBOLS = "%_$-!*";

    private final static int MIN_LENGTH = 9;

    private final static int MAX_LENGTH = 256;  //DB LIMIT

    public static boolean check(String password)
    {
        if(password==null || password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) return false;

        for (int i = 0; i < password.length(); i++)
            if(ALLOWED_CHARS.indexOf((char) password.charAt(i))==-1) return false;

        boolean valid = false;
        for (int i = 0; i < ALLOWED_UPPER_CHARS.length(); i++)
            if(password.indexOf((char) ALLOWED_UPPER_CHARS.charAt(i))!=-1) { valid = true; break; }
        if(!valid) return false;

        valid = false;
        for (int i = 0; i < ALLOWED_LOWER_CHARS.length(); i++)
            if(password.indexOf((char) ALLOWED_LOWER_CHARS.charAt(i))!=-1) { valid = true; break; }
        if(!valid) return false;

        valid = false;
        for (int i = 0; i < ALLOWED_DIGITS.length(); i++)
            if(password.indexOf((char) ALLOWED_DIGITS.charAt(i))!=-1) { valid = true; break; }
        if(!valid) return false;

        valid = false;
        for (int i = 0; i < ALLOWED_SYMBOLS.length(); i++)
            if(password.indexOf((char) ALLOWED_SYMBOLS.charAt(i))!=-1) { valid = true; break; }

        return valid;
    }

    public static String generate()
    {
        Random rand = new Random(System.currentTimeMillis());
        String res = "";

        for(int i=0; i<3; i++)
            res += ALLOWED_UPPER_CHARS.charAt(rand.nextInt(ALLOWED_UPPER_CHARS.length()));

        for(int i=0; i<3; i++)
            res += ALLOWED_LOWER_CHARS.charAt(rand.nextInt(ALLOWED_LOWER_CHARS.length()));

        for(int i=0; i<1; i++)
            res += ALLOWED_SYMBOLS.charAt(rand.nextInt(ALLOWED_SYMBOLS.length()));

        for(int i=0; i<2; i++)
            res += ALLOWED_DIGITS.charAt(rand.nextInt(ALLOWED_DIGITS.length()));

        return res;
    }

    public static String generateForJDBC()
    {
        Random rand = new Random(System.currentTimeMillis());
        String res = "";

        for(int i=0; i<3; i++)
            res += ALLOWED_UPPER_CHARS.charAt(rand.nextInt(ALLOWED_UPPER_CHARS.length()));

        for(int i=0; i<3; i++)
            res += ALLOWED_LOWER_CHARS.charAt(rand.nextInt(ALLOWED_LOWER_CHARS.length()));

        res += "_";

        for(int i=0; i<2; i++)
            res += ALLOWED_DIGITS.charAt(rand.nextInt(ALLOWED_DIGITS.length()));

        return res;
    }

}
