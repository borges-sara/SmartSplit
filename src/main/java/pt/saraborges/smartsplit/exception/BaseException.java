package pt.saraborges.smartsplit.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    private int code;

    public BaseException(String message, int code){
        super(message);
        this.code = code;
    }

    public BaseException(String message, int code, Throwable cause){
        super(message, cause);
        this.code = code;
    }
}
