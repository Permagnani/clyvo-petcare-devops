package br.com.fiap.clyvopetcare.repository;

import br.com.fiap.clyvopetcare.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}