package com.ciclocare.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "lembretes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lembretes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "tipo_procedimento", nullable = false, length = 100)
    private String tipoProcedimento;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDate data;

    @Column(name = "profissional_saude", length = 100)
    private String profissionalSaude;

    @Column(nullable = false, length = 100)
    private String clinica;

    @Column(nullable = false, length = 255)
    private String endereco;

    @Transient
    private Boolean ativo;

    public Boolean getAtivo() {
        if (this.data == null) {
            return false;
        }
        LocalDate hoje = LocalDate.now();
        return !this.data.isBefore(hoje);
    }

}
