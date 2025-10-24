package com.giacomopillitteri.eventi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // errori 404
    @ExceptionHandler(RisorsaNonTrovataException.class)
    public ResponseEntity<String> handleNotFound(RisorsaNonTrovataException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // errori 400
    @ExceptionHandler(PostiEsauritiException.class)
    public ResponseEntity<String> handleBadRequest(PostiEsauritiException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // errori 403
    @ExceptionHandler(AutorizzazioneNonValidaException.class)
    public ResponseEntity<String> handleForbidden(AutorizzazioneNonValidaException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

}