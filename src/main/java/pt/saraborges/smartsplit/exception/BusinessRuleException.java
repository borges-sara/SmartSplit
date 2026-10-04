package pt.saraborges.smartsplit.exception;

public class BusinessRuleException extends BaseException{
    public BusinessRuleException(String message, int code){
        super(message, 422);
    }
}
