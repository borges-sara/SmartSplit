package pt.saraborges.smartsplit.common.exception;

public class ConflictException extends BaseException{
    public ConflictException(String message, int code){
        super(message, 409);
    }
}
