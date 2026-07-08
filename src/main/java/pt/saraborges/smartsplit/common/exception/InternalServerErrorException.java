package pt.saraborges.smartsplit.common.exception;

public class InternalServerErrorException extends BaseException{
    public InternalServerErrorException(String message, int code){
        super(message, 500);
    }
}
