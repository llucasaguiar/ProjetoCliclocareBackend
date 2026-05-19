package com.ciclocare.service;

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

		LocalDate dataInicio = request.getUltimaMenstruacao();
		LocalDate dataFim = dataInicio.plusDays(request.getDuracaoMenstruacao() - 1);
		LocalDate proximaPrevisao = dataInicio.plusDays(request.getDuracaoCiclo());

		CicloMenstrual ciclo = CicloMenstrual.builder()
				.usuario(usuario)
				.dataInicio(dataInicio)
				.dataFim(dataFim)
				.proximaPrevisao(proximaPrevisao)
				.ultimaMenstruacao(request.getUltimaMenstruacao())
				.duracaoCiclo(request.getDuracaoCiclo())
				.duracaoMenstruacao(request.getDuracaoMenstruacao())
				.build();

		usuario.getCiclosMenstruais().add(ciclo);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
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
                .dataCriacao(usuario.getDataCriacao())
                .ultimaAtualizacao(usuario.getUltimaAtualizacao())
                .ativo(usuario.getAtivo())
                .build();
    }
}
