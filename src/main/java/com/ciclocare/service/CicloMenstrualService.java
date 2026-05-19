package com.ciclocare.service;

import com.ciclocare.dto.request.CicloMenstrualRequest;
import com.ciclocare.dto.response.CicloMenstrualResponse;
import com.ciclocare.dto.response.DashboardCicloResponse;
import com.ciclocare.dto.response.UsuarioResponse;
import com.ciclocare.entity.CicloMenstrual;
import com.ciclocare.entity.Usuario;
import com.ciclocare.enums.FaseCiclo;
import com.ciclocare.exception.ResourceNotFoundException;
import com.ciclocare.repository.CicloMenstrualRepository;
import com.ciclocare.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CicloMenstrualService {

    private final CicloMenstrualRepository cicloRepository;
	private final UsuarioRepository usuarioRepository;

    @Transactional
    public CicloMenstrualResponse criar(UUID usuarioId, CicloMenstrualRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        LocalDate proximaPrevisao = request.getUltimaMenstruacao()
                .plusDays(request.getDuracaoCiclo());

        CicloMenstrual ciclo = CicloMenstrual.builder()
                .usuario(usuario)
                .dataInicio(request.getUltimaMenstruacao())
                .dataFim(request.getUltimaMenstruacao().plusDays(request.getDuracaoMenstruacao() - 1))
                .ultimaMenstruacao(request.getUltimaMenstruacao())
				.duracaoCiclo(request.getDuracaoCiclo())
				.duracaoMenstruacao(request.getDuracaoMenstruacao())
                .proximaPrevisao(proximaPrevisao)
                .intensidadeFluxo(request.getIntensidadeFluxo())
                .build();

        CicloMenstrual cicloSalvo = cicloRepository.save(ciclo);
        return mapToResponse(cicloSalvo);
    }

    public CicloMenstrualResponse buscarPorId(UUID id) {
        CicloMenstrual ciclo = cicloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo menstrual não encontrado"));
        return mapToResponse(ciclo);
    }

    public List<CicloMenstrualResponse> buscarTodosPorUsuario(UUID usuarioId) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        return cicloRepository.findAllByUsuario(usuario)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CicloMenstrualResponse buscarUltimo(UUID usuarioId) {
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);
        CicloMenstrual ciclo = cicloRepository.findUltimoByUsuario(usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum ciclo registrado"));
        return mapToResponse(ciclo);
    }

    @Transactional
    public CicloMenstrualResponse atualizar(UUID id, CicloMenstrualRequest request) {
        CicloMenstrual ciclo = cicloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo menstrual não encontrado"));

        ciclo.setDataInicio(request.getDataInicio());
        ciclo.setDataFim(request.getDataFim());
        ciclo.setDuracaoCiclo(request.getDuracaoCiclo());
        ciclo.setDuracaoMenstruacao(request.getDuracaoMenstruacao());
        ciclo.setUltimaMenstruacao(request.getUltimaMenstruacao());
        ciclo.setProximaPrevisao(request.getUltimaMenstruacao().plusDays(request.getDuracaoCiclo()));
        ciclo.setIntensidadeFluxo(request.getIntensidadeFluxo());

        CicloMenstrual cicloAtualizado = cicloRepository.save(ciclo);
        return mapToResponse(cicloAtualizado);
    }

    @Transactional
    public void deletar(UUID id) {
        CicloMenstrual ciclo = cicloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo menstrual não encontrado"));
        cicloRepository.delete(ciclo);
    }

    private CicloMenstrualResponse mapToResponse(CicloMenstrual ciclo) {
        return CicloMenstrualResponse.builder()
                .id(ciclo.getId())
                .usuarioId(ciclo.getUsuario().getId())
                .dataInicio(ciclo.getDataInicio())
                .dataFim(ciclo.getDataFim())
                .duracaoCiclo(ciclo.getDuracaoCiclo())
                .duracaoMenstruacao(ciclo.getDuracaoMenstruacao())
                .ultimaMenstruacao(ciclo.getUltimaMenstruacao())
                .proximaPrevisao(ciclo.getProximaPrevisao())
                .intensidadeFluxo(ciclo.getIntensidadeFluxo())
                .criadoEm(ciclo.getCriadoEm())
                .build();
    }

	public String gerarMensagem(FaseCiclo faseCiclo) {
		return switch(faseCiclo) {
			case MENSTRUAL -> "Seu corpo está em fase de renovação.";
			case FOLICULAR -> "Mais energia e disposição hoje.";
			case OVULACAO -> "Alta fertilidade no momento.";
			case LUTEA -> "Momento ideal para desacelerar.";
		};
	}

	public DashboardCicloResponse exibirDashboard(UUID idUsuaria) {
		Usuario usuaria = usuarioRepository.findById(idUsuaria)
				.orElseThrow(() ->
						new ResourceNotFoundException("Usuária não encontrada."));

		CicloMenstrual cicloAtual =
				cicloRepository.findByUsuarioOrderByDataInicioDesc(usuaria)
						.orElseThrow(() ->
								new ResourceNotFoundException("Nenhum ciclo encontrado."));

		Integer diaCiclo = calcularDiaCiclo(
				cicloAtual.getUltimaMenstruacao(),
				cicloAtual.getDuracaoCiclo());

		FaseCiclo faseCiclo = calcularFaseAtual(
				diaCiclo,
				cicloAtual.getDuracaoCiclo(),
				cicloAtual.getDuracaoMenstruacao()
		);

		return DashboardCicloResponse.builder()
				.diaCiclo(diaCiclo)
				.faseCiclo(faseCiclo)
				.mensagem(gerarMensagem(faseCiclo))
				.ultimaMenstruacao(
						cicloAtual.getUltimaMenstruacao()
				)
				.duracaoCiclo(
						cicloAtual.getDuracaoCiclo()
				)
				.duracaoMenstruacao(
						cicloAtual.getDuracaoMenstruacao()
				)
				.build();
	}

	public FaseCiclo calcularFaseAtual(
			Integer diaCiclo,
			Integer duracaoCiclo,
			Integer duracaoMenstruacao) {
		if (diaCiclo <= duracaoMenstruacao) {
			return FaseCiclo.MENSTRUAL;
		}

		int ovulacao = duracaoCiclo - 14;

		if (diaCiclo < ovulacao) {
			return FaseCiclo.FOLICULAR;
		}

		if (diaCiclo == ovulacao) {
			return FaseCiclo.OVULACAO;
		}

		return FaseCiclo.LUTEA;
	}

	public Integer calcularDiaCiclo(LocalDate ultimaMenstruacao, Integer duracaoCiclo) {
		Long dias = ChronoUnit.DAYS.between(
				ultimaMenstruacao,
				LocalDate.now()
		);

		return (int) (dias % duracaoCiclo) + 1;
	}
}
