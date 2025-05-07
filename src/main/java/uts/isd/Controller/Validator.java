package uts.isd.Controller;

import java.util.regex.Pattern;

//Validates any string

public class Validator
{

    public static final String NAME = "([A-Z][a-z]+[\\s])+[A-Z][a-z]*";
    public static final String EMAIL = "([a-zA-Z0-9]+)(([._-])([a-zA-Z0-9]+))*(@)([a-z]+)(.)([a-z]{3})((([.])[a-z]{0,2})*)";
    public static final String PASSWORD = "[a-z0-9]{4,}";

    /**
     * Checks if the string contains any letter.
     */
    public static boolean containsLetter(String s) {
        if (s == null) return false;
        return s.chars().anyMatch(Character::isLetter);
    }

    /**
     * Checks if the string contains any digit.
     */
    public static boolean containsNumber(String s) {
        if (s == null) return false;
        return s.chars().anyMatch(Character::isDigit);
    }


    /**
     * Validates the input string against the provided regular expression.
     */
    public static boolean validate(String string, String regularExpression) {
        if (string == null || regularExpression == null) return false;
        Pattern pattern = Pattern.compile(regularExpression);
        return pattern.matcher(string).matches();
    }

    /**
     * Checks any String for an apostrophe to avoid SQL Injection.
     *
     * @param s String to check.
     * @return True if s contains an apostrophe.
     */
    public static boolean containsApostrophe(String s) {
        if (s == null) return false;
        return s.indexOf('\'') >= 0;
    }
}
