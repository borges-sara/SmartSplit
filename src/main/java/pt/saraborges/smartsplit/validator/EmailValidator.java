package pt.saraborges.smartsplit.validator;

import java.util.regex.Pattern;

public class EmailValidator extends BasicValidator {
    private static final Pattern HAS_EMAIL_CHARACTER = Pattern.compile("^(.+)@(\\S+)$");

    public EmailValidator(){
        super("Email Validator");
    }

    public void validate(String email){
        if(email == null || email.isEmpty()){
            fail("Email cannot be null or empty.");
        } else if (!HAS_EMAIL_CHARACTER.matcher(email).find()) {
            fail("Email must be in the following format 'username@domain.com'.");
        }
    }
}
