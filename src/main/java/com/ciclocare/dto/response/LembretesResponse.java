package com.ciclocare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LembretesResponse {
    private UUID id;
    private UUID usuarioId;
    private String tipoProcedimento;
    private LocalDate data;
    private String profissionalSaude;
    private String clinica;
    private String endereco;
    private Boolean ativo;
}
