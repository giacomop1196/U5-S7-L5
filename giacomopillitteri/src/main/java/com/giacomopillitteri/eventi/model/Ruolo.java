package com.giacomopillitteri.eventi.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ruoli")
public class Ruolo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NomeRuolo nome;

    public enum NomeRuolo {
        UTENTE_NORMALE,
        ORGANIZZATORE_EVENTI
    }
}