package pt.saraborges.smartsplit.validator;

import java.util.regex.Pattern;

public class PasswordValidator implements BasicValidator {
    private static final Pattern UPPER_CASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE = Pattern.compile("[a-z]");
    private static final Pattern DIGITS = Pattern.compile("\\d");
    private static final Pattern SPECIAL = Pattern.compile("[^A-Za-z0-9]");
    private static final int MIN_LENGTH = 8;

    /**
     * Password constraints:
     * must have letters in upper and lower case
     * must have numbers
     * must have special characters
     * @param plainText Password plain content
     */
    public static void validate(String plainText){
        if(plainText == null || plainText.length() < MIN_LENGTH){
            BasicValidator.fail("Password must have at least 8 characters.");
        } else if(!UPPER_CASE.matcher(plainText).find()){
            BasicValidator.fail("Password must have upper case letters.");
        } else if(!LOWER_CASE.matcher(plainText).find()){
            BasicValidator.fail("Password must have lower case letters.");
        } else if (!DIGITS.matcher(plainText).find()) {
            BasicValidator.fail("Password must have digits.");
        } else if (!SPECIAL.matcher(plainText).find()){
            BasicValidator.fail("Password must have special characters.");
        }
    }
}
