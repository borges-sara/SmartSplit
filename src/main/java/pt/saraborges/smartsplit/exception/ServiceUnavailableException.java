package pt.saraborges.smartsplit.exception;

public class ServiceUnavailableException extends BaseException{
    public ServiceUnavailableException(String message){
        super(message, 503);
    }
}
