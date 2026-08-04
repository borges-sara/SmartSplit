package pt.saraborges.smartsplit.validator;

import lombok.Getter;
import lombok.Setter;
import pt.saraborges.smartsplit.exception.ValidationException;

public abstract class BasicValidator {

    @Getter
    @Setter
    private String validatorName;

    public BasicValidator(String name){
        this.validatorName = name;
    }

    public void fail(String message){
        throw new ValidationException(validatorName + ": " + message);
    }
}
