package sucursal.api.handler;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import sucursal.api.exceptions.ErrorCode;
import sucursal.api.exceptions.ProblemDetailFactory;
import sucursal.api.exceptions.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        List<String> fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .toList();

        String URI = ((ServletWebRequest) request).getRequest().getRequestURI();

        ProblemDetail problemDetail = ProblemDetailFactory.from(ErrorCode.VALIDATION_ERROR, "Los datos enviados no son válidos", URI, 
            Map.of("errors", fieldErrors,
            "counts", fieldErrors.size()));

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException exception, HttpServletRequest request){
        return ProblemDetailFactory.from(ErrorCode.RESOURCE_NOT_FOUND, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception exception, HttpServletRequest request){
        return ProblemDetailFactory.from(ErrorCode.INTERNAL_ERROR, "Ha ocurrido un error interno", request.getRequestURI());
    }

}
