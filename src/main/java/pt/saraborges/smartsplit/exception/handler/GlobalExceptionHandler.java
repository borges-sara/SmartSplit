package pt.saraborges.smartsplit.exception.handler;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pt.saraborges.smartsplit.exception.BaseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex){
        return ResponseEntity
                .status(ex.getCode())
                .body(new ErrorResponse(ex.getMessage(), ex.getCode()));
    }
}
