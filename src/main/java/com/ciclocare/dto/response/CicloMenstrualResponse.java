package com.ciclocare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CicloMenstrualResponse {

    private UUID id;
    private UUID usuarioId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer duracaoCiclo;
    private Integer duracaoMenstruacao;
	private Integer media3UltimosCiclos;
    private LocalDate ultimaMenstruacao;
    private LocalDate proximaPrevisao;
    private String intensidadeFluxo;
    private LocalDateTime criadoEm;
	private LocalDate janelaFertilInicio;
	private LocalDate janelaFertilFim;
	private LocalDate previsaoOvulacao;
	private List<LocalDate> ciclosFuturos;

}
