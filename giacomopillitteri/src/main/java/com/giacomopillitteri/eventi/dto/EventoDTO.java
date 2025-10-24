package com.giacomopillitteri.eventi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoDTO {
    private String titolo;
    private String descrizione;
    private LocalDateTime data;
    private String luogo;
    private Integer postiDisponibili;
}
