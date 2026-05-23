package br.com.fiap.clyvopetcare.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "TB_ALERTA")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALERTA")
    private Long id;

    @NotBlank
    @Column(name = "TP_ALERTA", nullable = false, length = 50)
    private String tipo;

    @NotBlank
    @Column(name = "DS_ALERTA", nullable = false, length = 255)
    private String descricao;

    @NotNull
    @FutureOrPresent
    @Column(name = "DT_ALERTA", nullable = false)
    private LocalDate dataAlerta;

    @Column(name = "ST_ALERTA", nullable = false, length = 30)
    private String status = "PENDENTE";

    @ManyToOne
    @JoinColumn(name = "ID_PET", nullable = false)
    @JsonBackReference
    private Pet pet;

    public Alerta() {
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataAlerta() {
        return dataAlerta;
    }

    public void setDataAlerta(LocalDate dataAlerta) {
        this.dataAlerta = dataAlerta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}