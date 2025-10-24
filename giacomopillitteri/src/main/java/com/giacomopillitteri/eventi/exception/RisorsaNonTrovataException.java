package com.giacomopillitteri.eventi.exception;

public class RisorsaNonTrovataException extends RuntimeException {
    public RisorsaNonTrovataException(String risorsa, Long id) {
        super(risorsa + " con ID " + id + " non trovato.");
    }
}