package com.ciclocare.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LembretesRequest {

    @NotBlank(message = "Tipo de procedimento é obrigatório.")
    @Size(max = 100)
    private String tipoProcedimento;

    @NotNull(message = "Data do agendamento é obrigatória.")
    private LocalDate data;

    @Size(max = 100)
    private String profissionalSaude;

    @NotBlank(message = "A clínica ou local é obrigatória.")
    @Size(max = 100)
    private String clinica;

    @NotBlank(message = "Endereço é obrigatório.")
    @Size(max = 255)
    private String endereco;
}
