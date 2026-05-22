package com.ciclocare.service;

import com.ciclocare.dto.request.CicloMenstrualRequest;
import com.ciclocare.dto.request.RegisterRequest;
import com.ciclocare.dto.request.UpdateProfileRequest;
import com.ciclocare.dto.response.UsuarioResponse;
import com.ciclocare.entity.CicloMenstrual;
import com.ciclocare.entity.Usuario;
import com.ciclocare.exception.ResourceNotFoundException;
import com.ciclocare.exception.ValidationException;
import com.ciclocare.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
	private final CicloMenstrualService cicloMenstrualService;

    @Transactional
    public UsuarioResponse registrar(RegisterRequest request) {
        // Validar se email já existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email já está registrado");
        }

        // Criar novo usuário
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .dataNascimento(request.getNascimento())
                .senha(passwordEncoder.encode(request.getSenha()))
                .ativo(true)
                .build();
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		// Criar primeiro ciclo
		var dadosCiclo = request.getDadosCiclo();

		LocalDate dataInicio = request.getDadosCiclo().getDataInicio();
		LocalDate dataFim = dataInicio.plusDays(request.getDadosCiclo().getDuracaoMenstruacao() - 1);
		LocalDate proximaPrevisao = dataInicio.plusDays(request.getDadosCiclo().getDuracaoCiclo());

		CicloMenstrualRequest cicloMenstrualRequest = CicloMenstrualRequest.builder()
				.dataInicio(request.getDadosCiclo().getDataInicio())
				.dataFim(request.getDadosCiclo().getDataFim())
				.ultimaMenstruacao(request.getDadosCiclo().getUltimaMenstruacao())
				.duracaoCiclo(request.getDadosCiclo().getDuracaoCiclo())
				.duracaoMenstruacao(request.getDadosCiclo().getDuracaoMenstruacao())
				.proximaPrevisao(request.getDadosCiclo().getProximaPrevisao())
				.intensidadeFluxo(request.getDadosCiclo().getIntensidadeFluxo())
				.build();

		cicloMenstrualService.criar(usuario.getId(), cicloMenstrualRequest);
        return mapToResponse(usuarioSalvo);
    }

    public UsuarioResponse buscarPorId(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return mapToResponse(usuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public UsuarioResponse atualizarPerfil(UUID id, UpdateProfileRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setDataNascimento(request.getDataNascimento());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        return mapToResponse(usuarioAtualizado);
    }

    @Transactional
    public void deletarConta(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }

    public UsuarioResponse mapToResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataNascimento(usuario.getDataNascimento())
                .dataCriacao(usuario.getDataCriacao())
                .ultimaAtualizacao(usuario.getUltimaAtualizacao())
                .ativo(usuario.getAtivo())
                .build();
    }
}
