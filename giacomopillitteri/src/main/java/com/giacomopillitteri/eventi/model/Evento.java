package com.giacomopillitteri.eventi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "eventi")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titolo;
    private String descrizione;
    private LocalDateTime data;
    private String luogo;
    private int postiDisponibili;

    @ManyToOne
    @JoinColumn(name = "creatore_id")
    private Utente creatore;

    // Lista degli Utenti (Utenti Normali) che hanno prenotato
    @ManyToMany
    @JoinTable(
            name = "prenotazioni",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "utente_id")
    )
    private Set<Utente> partecipanti = new HashSet<>();
}