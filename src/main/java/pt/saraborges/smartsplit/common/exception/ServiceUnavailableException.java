package pt.saraborges.smartsplit.common.exception;

public class ServiceUnavailableException extends BaseException{
    public ServiceUnavailableException(String message, int code){
        super(message, 503);
    }
}
