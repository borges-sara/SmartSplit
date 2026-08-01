package pt.saraborges.smartsplit.exception.handler;

public class ErrorResponse {
    private final String message;
    private final int code;

    public ErrorResponse(String message, int code){
        this.message = message;
        this.code = code;
    }
}
