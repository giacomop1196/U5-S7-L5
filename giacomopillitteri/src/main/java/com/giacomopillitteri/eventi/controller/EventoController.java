package com.giacomopillitteri.eventi.controller;

import com.giacomopillitteri.eventi.dto.EventoDTO;
import com.giacomopillitteri.eventi.exception.AutorizzazioneNonValidaException;
import com.giacomopillitteri.eventi.exception.PostiEsauritiException;
import com.giacomopillitteri.eventi.exception.RisorsaNonTrovataException;
import com.giacomopillitteri.eventi.model.Evento;
import com.giacomopillitteri.eventi.service.AuthService;
import com.giacomopillitteri.eventi.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/eventi")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;
    private final AuthService authService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {

            String username;

            if (authentication.getPrincipal() instanceof User) {
                username = ((User) authentication.getPrincipal()).getUsername();
            } else {
                username = authentication.getName();
            }
            return authService.findIdByUsername(username);
        }
        throw new AutorizzazioneNonValidaException("Utente non autenticato o contesto di sicurezza non valido.");
    }

    
    // LISTA DI TUTTI GLI EVENTI
    @GetMapping
    public ResponseEntity<List<Evento>> getAllEventi() {
        List<Evento> eventi = eventoService.findAll();
        return ResponseEntity.ok(eventi);
    }

    // DETTAGLIO EVENTO
    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEventoById(@PathVariable Long id) {
        Evento evento = eventoService.findById(id);
        return ResponseEntity.ok(evento);
    }

    // CREAZIONE EVENTO
    @PostMapping
    public ResponseEntity<Evento> creaEvento(@RequestBody EventoDTO eventodto) {
        Long creatoreId = getCurrentUserId();
        Evento eventoSalvato = eventoService.crea(eventodto, creatoreId);
        return new ResponseEntity<>(eventoSalvato, HttpStatus.CREATED);
    }

    // MODIFICA EVENTO
    @PutMapping("/{id}")
    public ResponseEntity<Evento> updateEvento(@PathVariable Long id, @RequestBody EventoDTO datiAggiornati) {
        Long userId = getCurrentUserId();
        Evento eventoAggiornato = eventoService.modifica(id, datiAggiornati, userId);
        return ResponseEntity.ok(eventoAggiornato);

    }

    // ELIMINAZIONE EVENTO
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvento(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        eventoService.elimina(id, userId);
    }

    // PRENOTAZIONE POSTO
    @PostMapping("/{id}/prenota")
    @PreAuthorize("hasAuthority('UTENTE_NORMALE')")
    public ResponseEntity<String> prenotaPosto(@PathVariable Long id) {
        Long utenteId = getCurrentUserId();
        eventoService.prenota(id, utenteId);
        return ResponseEntity.ok("Posto prenotato con successo.");
    }
}