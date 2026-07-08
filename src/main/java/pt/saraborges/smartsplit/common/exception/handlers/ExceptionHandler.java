package pt.saraborges.smartsplit.common.exception.handlers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pt.saraborges.smartsplit.common.exception.BaseException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex){
        return ResponseEntity
                .status(ex.getCode())
                .body(new ErrorResponse(ex.getMessage(), ex.getCode()));
    }
}
