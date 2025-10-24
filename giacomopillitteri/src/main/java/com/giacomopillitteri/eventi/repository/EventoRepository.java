package com.giacomopillitteri.eventi.repository;

import com.giacomopillitteri.eventi.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByCreatoreId(Long creatoreId);
}