package pt.saraborges.smartsplit.validator;

import java.util.regex.Pattern;

public class EmailValidator implements BasicValidator {
    private static final Pattern HAS_EMAIL_CHARACTER = Pattern.compile("^(.+)@(\\S+)$");

    public static void validate(String email){
        if(email == null || email.isEmpty()){
            BasicValidator.fail("Email cannot be null or empty.");
        } else if (!HAS_EMAIL_CHARACTER.matcher(email).find()) {
            BasicValidator.fail("Email must be in the following format 'username@domain.com'.");
        }
    }
}
