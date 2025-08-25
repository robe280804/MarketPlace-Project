package com.market_place.product_service.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValidEx(MethodArgumentNotValidException ex, WebRequest request){
        Map<String, Object> response = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                response.put(err.getField(), err.getDefaultMessage()));

        response.put("timestamps", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST);
        response.put("error", "Errore di validazione");
        response.put("message", ex.getMessage());
        response.put("path", extractPath(request));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<Object> handlerServerEx(FeignException.InternalServerError ex, WebRequest request){
        return generateResponse("Errore lato server", ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<Object> handleProductNotFoundEx(ProductNotFound ex, WebRequest request){
        return generateResponse("Prodotto non trovato", ex, HttpStatus.NOT_FOUND, request);
    }

    private static ResponseEntity<Object> generateResponse(
            String error, Exception ex, HttpStatus status, WebRequest request
    ){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamps", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", ex.getMessage());
        response.put("path", extractPath(request));

        return new ResponseEntity<>(response, status);
    }

    private static String extractPath(WebRequest request){
        return request.getDescription(false).replace("uri=", "");
    }
}
