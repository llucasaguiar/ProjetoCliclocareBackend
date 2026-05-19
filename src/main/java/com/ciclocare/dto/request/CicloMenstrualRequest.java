package com.ciclocare.dto.request;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CicloMenstrualRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID idCiclo;

    @NotNull(message = "Duração do ciclo é obrigatória")
    @Min(value = 20, message = "Duração do ciclo deve ser mínimo 20 dias")
    private Integer duracaoCiclo;

    @NotNull(message = "Duração da menstruação é obrigatória")
    @Min(value = 1, message = "Duração da menstruação deve ser mínimo 1 dia")
    private Integer duracaoMenstruacao;

    @NotNull(message = "Última menstruação é obrigatória")
    private LocalDate ultimaMenstruacao;

	@NotNull(message = "Data de início é obrigatória")
	private LocalDate dataInicio;

	@NotNull(message = "Data de término é obrigatória")
	private LocalDate dataFim;

	private LocalDate proximaPrevisao;


	private String intensidadeFluxo;

}
