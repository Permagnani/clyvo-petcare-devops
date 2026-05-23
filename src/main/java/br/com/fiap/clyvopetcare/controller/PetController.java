package br.com.fiap.clyvopetcare.controller;

import br.com.fiap.clyvopetcare.model.Pet;
import br.com.fiap.clyvopetcare.repository.PetRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetRepository petRepository;

    public PetController(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @GetMapping
    public ResponseEntity<List<Pet>> listar() {
        return ResponseEntity.ok(petRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> buscarPorId(@PathVariable Long id) {
        return petRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pet> criar(@Valid @RequestBody Pet pet) {
        Pet salvo = petRepository.save(pet);
        return ResponseEntity
                .created(URI.create("/pets/" + salvo.getId()))
                .body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> atualizar(@PathVariable Long id, @Valid @RequestBody Pet dados) {
        return petRepository.findById(id)
                .map(pet -> {
                    pet.setNome(dados.getNome());
                    pet.setEspecie(dados.getEspecie());
                    pet.setRaca(dados.getRaca());
                    pet.setPeso(dados.getPeso());
                    pet.setDataNascimento(dados.getDataNascimento());
                    pet.setNomeResponsavel(dados.getNomeResponsavel());
                    pet.setObservacoes(dados.getObservacoes());

                    Pet atualizado = petRepository.save(pet);
                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!petRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        petRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
