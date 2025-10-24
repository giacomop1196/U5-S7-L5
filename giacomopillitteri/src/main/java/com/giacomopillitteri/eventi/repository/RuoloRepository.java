package com.giacomopillitteri.eventi.repository;

import com.giacomopillitteri.eventi.model.Ruolo;
import com.giacomopillitteri.eventi.model.Ruolo.NomeRuolo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RuoloRepository extends JpaRepository<Ruolo, Long> {
    Optional<Ruolo> findByNome(NomeRuolo nome);
}
