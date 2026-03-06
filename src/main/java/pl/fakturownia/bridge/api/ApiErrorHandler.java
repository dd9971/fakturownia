package pl.fakturownia.bridge.api;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.fakturownia.client.invoker.ApiException;

@RestControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Map<String, Object> handleApi(ApiException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Błąd komunikacji z API Fakturownia");
        body.put("statusCode", exception.getCode());
        body.put("responseBody", exception.getResponseBody());
        return body;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException exception) {
        return Map.of("message", "Niepoprawne dane wejściowe", "details", exception.getMessage());
    }
}
