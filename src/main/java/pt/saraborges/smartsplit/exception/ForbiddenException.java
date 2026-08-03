package pt.saraborges.smartsplit.exception;

public class ForbiddenException extends BaseException{
    public ForbiddenException(String message, int code){
        super(message, 403);
    }
}
