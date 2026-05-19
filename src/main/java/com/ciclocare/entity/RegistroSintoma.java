package com.ciclocare.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "registro_sintomas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroSintoma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataRegistro;
    private String categoria;
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
