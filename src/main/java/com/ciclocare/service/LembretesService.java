package com.ciclocare.service;


import com.ciclocare.dto.request.LembretesRequest;

import com.ciclocare.dto.response.LembretesResponse;
import com.ciclocare.entity.Lembretes;
import com.ciclocare.exception.ResourceNotFoundException;
import com.ciclocare.repository.LembretesRepository;
import com.ciclocare.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LembretesService {

    private final LembretesRepository lembretesRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Lembretes criar(Lembretes lembretes) {
        log.info("Agendado novo lembrete: {}", lembretes.getTipoProcedimento());
        try {
            Lembretes lembreteSalvo = lembretesRepository.save(lembretes);
            log.info("Lembrete agendado com sucesso. ID: {}, Tipo: {}", lembreteSalvo.getId(), lembreteSalvo.getTipoProcedimento());
            return lembreteSalvo;
        } catch (Exception e) {
            log.error("Falha ao agendar lembrete '{}': {}", lembretes.getTipoProcedimento(), e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public List<Lembretes> listar() {
        log.info("Buscando todos os lembretes agendados");
        try {
            List<Lembretes> lembretes = lembretesRepository.findAll();
            return lembretes;
        } catch (Exception e) {
            log.error("Falha ao buscar lembretes: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public LembretesResponse atualizar(UUID id, LembretesRequest request) {
        Lembretes lembretes = lembretesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de humor não encontrado"));

        lembretes.setTipoProcedimento(request.getTipoProcedimento());
        lembretes.setData(request.getData());
        lembretes.setProfissionalSaude(request.getProfissionalSaude());
        lembretes.setClinica(request.getClinica());
        lembretes.setEndereco(request.getEndereco());

        Lembretes lembreteAtualizado = lembretesRepository.save(lembretes);
        return mapToResponse(lembreteAtualizado);
    }

    private LembretesResponse mapToResponse(Lembretes lembreteAtualizado) {
        return LembretesResponse.builder()
                .id(lembreteAtualizado.getId())
                .usuarioId(lembreteAtualizado.getUsuario().getId())
                .tipoProcedimento(lembreteAtualizado.getTipoProcedimento())
                .data(lembreteAtualizado.getData())
                .profissionalSaude(lembreteAtualizado.getProfissionalSaude())
                .clinica(lembreteAtualizado.getClinica())
                .endereco(lembreteAtualizado.getEndereco())
                .ativo(lembreteAtualizado.getAtivo())
                .build();
    }

    @Transactional
    public void excluir(UUID id) {
        log.info("Excluindo lembrete ID: {}", id);
        if(lembretesRepository.existsById(id)) {
            String mensagem = String.format("Falha ao excluir. Lembrete não encontrado com o ID: %d", id);
            log.warn(mensagem);
            throw new RuntimeException(mensagem);
        }
        try {
            lembretesRepository.deleteById(id);
            log.info("Lembrete ID: {} excluído com sucesso.", id);
        } catch (Exception e) {
            log.error("Erro ao excluir lembrete ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
        lembretesRepository.deleteById(id);
    }

}
