package com.giacomopillitteri.eventi.exception;

public class AutorizzazioneNonValidaException extends RuntimeException {
    public AutorizzazioneNonValidaException(String messaggio) {
        super(messaggio);
    }
}
