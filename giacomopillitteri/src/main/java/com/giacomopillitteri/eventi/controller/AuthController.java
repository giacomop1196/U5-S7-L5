package com.giacomopillitteri.eventi.controller;

import com.giacomopillitteri.eventi.model.Utente;
import com.giacomopillitteri.eventi.service.AuthService;
import com.giacomopillitteri.eventi.dto.RegistrazioneRequest;
import com.giacomopillitteri.eventi.model.Ruolo.NomeRuolo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registrazione")
    public ResponseEntity<String> registraUtente(@RequestBody RegistrazioneRequest request) {
        try {
            authService.registraUtente(request.getUsername(), request.getPassword(), NomeRuolo.UTENTE_NORMALE);
            return new ResponseEntity<>("Registrazione Utente avvenuta con successo!", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}