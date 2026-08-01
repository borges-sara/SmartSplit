package pt.saraborges.smartsplit.entity.user.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pt.saraborges.smartsplit.validator.EmailValidator;

@AllArgsConstructor
public class Email {

    @Getter
    @Setter //TODO: configure setter: it must require password
    private String email;

    private EmailValidator validator;

    private Email(String email){
        this.email = email;
    }

    //TODO: include validation to check if email is already registered
    public static Email newEmail(String email, EmailValidator validator){
        validator.validate(email);
        return new Email(email);
    }
}
