package pt.saraborges.smartsplit.common.validators;

import lombok.Getter;
import lombok.Setter;
import pt.saraborges.smartsplit.common.exception.ValidationException;

public class BasicValidator {

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
