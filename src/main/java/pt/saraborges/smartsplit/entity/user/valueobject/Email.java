package pt.saraborges.smartsplit.entity.user.valueobject;

import lombok.Getter;
import lombok.Setter;
import pt.saraborges.smartsplit.validator.EmailValidator;

public class Email {

    @Getter
    @Setter //TODO: configure setter: it must require password
    private String email;

    private Email(String email){
        this.email = email;
    }

    //TODO: include validation to check if email is already registered
    public static Email newEmail(String email, EmailValidator validator){
        validator.validate(email);
        return new Email(email);
    }

    /**
     * Use when loading an already-validated email from the DB
     */
    public static Email fromExisting(String email){
        return new Email(email);
    }

    @Override
    public String toString() {
        return email;
    }
}
