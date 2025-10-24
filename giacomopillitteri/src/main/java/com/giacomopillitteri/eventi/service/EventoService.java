package com.giacomopillitteri.eventi.service;

// Nel package com.giacomopillitteri.eventi.service

import com.giacomopillitteri.eventi.dto.EventoDTO;
import com.giacomopillitteri.eventi.exception.AutorizzazioneNonValidaException;
import com.giacomopillitteri.eventi.exception.PostiEsauritiException;
import com.giacomopillitteri.eventi.exception.RisorsaNonTrovataException;
import com.giacomopillitteri.eventi.model.Evento;
import com.giacomopillitteri.eventi.model.Utente;
import com.giacomopillitteri.eventi.repository.EventoRepository;
import com.giacomopillitteri.eventi.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UtenteRepository utenteRepository;

    // LOGICA DI LETTURA

    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    public Evento findById(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RisorsaNonTrovataException("Evento", id));
    }

    // LOGICA DI CREAZIONE (Solo Organizzatore)

    @Transactional
    public Evento crea(EventoDTO eventodto, Long creatoreId) {
        Utente creatore = utenteRepository.findById(creatoreId)
                .orElseThrow(() -> new RisorsaNonTrovataException("Utente", creatoreId));

        Evento evento = new Evento();
        evento.setTitolo(eventodto.getTitolo());
        evento.setDescrizione(eventodto.getDescrizione());
        evento.setData(eventodto.getData());
        evento.setLuogo(eventodto.getLuogo());
        evento.setPostiDisponibili(eventodto.getPostiDisponibili() != null ? eventodto.getPostiDisponibili() : 0);
        evento.setCreatore(creatore);

        return eventoRepository.save(evento);
    }

    // LOGICA DI MODIFICA (Solo Organizzatore e solo il creatore)

    @Transactional
    public Evento modifica(Long id, EventoDTO datiAggiornati, Long userId) {
        Evento evento = findById(id);

        // Verifica che l'utente sia il creatore dell'evento
        if (!evento.getCreatore().getId().equals(userId)) {
            throw new AutorizzazioneNonValidaException("Non sei autorizzato a modificare questo evento.");
        }

        evento.setTitolo(datiAggiornati.getTitolo());
        evento.setDescrizione(datiAggiornati.getDescrizione());
        evento.setData(datiAggiornati.getData());
        evento.setLuogo(datiAggiornati.getLuogo());
        evento.setPostiDisponibili(datiAggiornati.getPostiDisponibili());

        return eventoRepository.save(evento);
    }

    // LOGICA DI ELIMINAZIONE (Solo Organizzatore e solo il creatore)

    @Transactional
    public void elimina(Long id, Long userId) {
        Evento evento = findById(id);

        // Verifica che l'utente sia il creatore dell'evento
        if (!evento.getCreatore().getId().equals(userId)) {
            throw new AutorizzazioneNonValidaException("Non sei autorizzato a eliminare questo evento.");
        }

        eventoRepository.delete(evento);
    }

    // LOGICA DI PRENOTAZIONE (Solo Utente Normale)

    @Transactional
    public void prenota(Long eventoId, Long utenteId) {
        Evento evento = findById(eventoId);
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RisorsaNonTrovataException("Utente", utenteId));

        // Verifica disponibilità posti
        if (evento.getPostiDisponibili() <= 0) {
            throw new PostiEsauritiException(evento.getTitolo());
        }

        // verifica che l'utente non abbia già prenotato
        if (evento.getPartecipanti().contains(utente)) {
            throw new AutorizzazioneNonValidaException("Hai già prenotato un posto per questo evento.");
        }

        // se va bene esegue la prenotazione
        evento.getPartecipanti().add(utente);
        evento.setPostiDisponibili(evento.getPostiDisponibili() - 1);

        eventoRepository.save(evento);
    }
}