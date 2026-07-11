package pt.saraborges.smartsplit.common.exception;

public class ValidationException extends BaseException{
    public ValidationException(String message, int code) {
        super(message, 400);
    }
}
