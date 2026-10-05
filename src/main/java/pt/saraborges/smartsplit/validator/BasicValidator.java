package pt.saraborges.smartsplit.validator;

import pt.saraborges.smartsplit.exception.ValidationException;

public interface  BasicValidator {

    static void fail(String message) throws ValidationException {
        throw new ValidationException(message);
    }
}
