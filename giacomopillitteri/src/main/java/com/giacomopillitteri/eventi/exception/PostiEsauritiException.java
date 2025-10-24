package com.giacomopillitteri.eventi.exception;

public class PostiEsauritiException extends RuntimeException {
    public PostiEsauritiException(String titolo) {
        super("Spiacenti, i posti per l'evento '" + titolo + "' sono esauriti.");
    }
}
