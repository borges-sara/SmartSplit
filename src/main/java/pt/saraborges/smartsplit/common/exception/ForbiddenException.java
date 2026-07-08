package pt.saraborges.smartsplit.common.exception;

public class ForbiddenException extends BaseException{
    public ForbiddenException(String message, int code){
        super(message, 403);
    }
}
