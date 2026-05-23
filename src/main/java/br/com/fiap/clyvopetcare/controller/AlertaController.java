package br.com.fiap.clyvopetcare.controller;

import br.com.fiap.clyvopetcare.model.Alerta;
import br.com.fiap.clyvopetcare.model.Pet;
import br.com.fiap.clyvopetcare.repository.AlertaRepository;
import br.com.fiap.clyvopetcare.repository.PetRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alertas")
public class AlertaController {

    private final AlertaRepository alertaRepository;
    private final PetRepository petRepository;

    public AlertaController(AlertaRepository alertaRepository, PetRepository petRepository) {
        this.alertaRepository = alertaRepository;
        this.petRepository = petRepository;
    }

    @GetMapping
    public ResponseEntity<List<Alerta>> listar() {
        return ResponseEntity.ok(alertaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alerta> buscarPorId(@PathVariable Long id) {
        return alertaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Alerta>> listarPorPet(@PathVariable Long petId) {
        if (!petRepository.existsById(petId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(alertaRepository.findByPetId(petId));
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody AlertaRequest request) {
        Pet pet = petRepository.findById(request.petId())
                .orElse(null);

        if (pet == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("erro", "Pet nao encontrado"));
        }

        Alerta alerta = new Alerta();
        alerta.setTipo(request.tipo());
        alerta.setDescricao(request.descricao());
        alerta.setDataAlerta(request.dataAlerta());
        alerta.setStatus(
                request.status() == null || request.status().isBlank()
                        ? "PENDENTE"
                        : request.status()
        );
        alerta.setPet(pet);

        Alerta salvo = alertaRepository.save(alerta);

        return ResponseEntity
                .created(URI.create("/alertas/" + salvo.getId()))
                .body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody AlertaRequest request) {
        Alerta alerta = alertaRepository.findById(id).orElse(null);

        if (alerta == null) {
            return ResponseEntity.notFound().build();
        }

        Pet pet = petRepository.findById(request.petId()).orElse(null);

        if (pet == null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("erro", "Pet nao encontrado"));
        }

        alerta.setTipo(request.tipo());
        alerta.setDescricao(request.descricao());
        alerta.setDataAlerta(request.dataAlerta());
        alerta.setStatus(
                request.status() == null || request.status().isBlank()
                        ? "PENDENTE"
                        : request.status()
        );
        alerta.setPet(pet);

        return ResponseEntity.ok(alertaRepository.save(alerta));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!alertaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        alertaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public record AlertaRequest(
            @NotNull Long petId,
            @NotBlank String tipo,
            @NotBlank String descricao,
            @NotNull @FutureOrPresent LocalDate dataAlerta,
            String status
    ) {
    }
}