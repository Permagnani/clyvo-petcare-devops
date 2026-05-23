package br.com.fiap.clyvopetcare.repository;

import br.com.fiap.clyvopetcare.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByPetId(Long petId);
}