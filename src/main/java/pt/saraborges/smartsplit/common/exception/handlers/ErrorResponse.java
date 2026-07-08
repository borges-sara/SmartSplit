package pt.saraborges.smartsplit.common.exception.handlers;

public class ErrorResponse {
    private final String message;
    private final int code;

    public ErrorResponse(String message, int code){
        this.message = message;
        this.code = code;
    }
}
