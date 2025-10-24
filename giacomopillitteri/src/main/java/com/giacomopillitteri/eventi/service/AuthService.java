package com.giacomopillitteri.eventi.service;

import com.giacomopillitteri.eventi.exception.RisorsaNonTrovataException;
import com.giacomopillitteri.eventi.model.Utente;
import com.giacomopillitteri.eventi.repository.RuoloRepository;
import com.giacomopillitteri.eventi.repository.UtenteRepository;
import com.giacomopillitteri.eventi.model.Ruolo.NomeRuolo; // Importa l'Enum del Ruolo
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.giacomopillitteri.eventi.model.Ruolo;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final RuoloRepository ruoloRepository;

    // carica l'utente per username durante il login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = utenteRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));
        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(utente.getRuolo().getNome().name()));

        return new User(utente.getUsername(), utente.getPassword(), authorities);
    }

    // Metodo di Registrazione
    public Utente registraUtente(String username, String rawPassword, NomeRuolo nomeRuolo) {
        if (utenteRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username già in uso!");
        }

        // Recupera il ruolo dal DB
        Ruolo ruolo = ruoloRepository.findByNome(nomeRuolo)
                .orElseThrow(() -> new RuntimeException("Ruolo non trovato: " + nomeRuolo));

        Utente nuovoUtente = new Utente();
        nuovoUtente.setUsername(username);
        nuovoUtente.setPassword(passwordEncoder.encode(rawPassword));
        nuovoUtente.setRuolo(ruolo);

        return utenteRepository.save(nuovoUtente);
    }

    // find id da username
    public Long findIdByUsername(String username) {
        return utenteRepository.findByUsername(username)
                .map(Utente::getId)
                .orElseThrow(() -> new RisorsaNonTrovataException("Utente", null));
    }
}