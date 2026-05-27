package com.ciclocare.controller;

import com.ciclocare.dto.request.LembretesRequest;
import com.ciclocare.dto.response.LembretesResponse;
import com.ciclocare.entity.Lembretes;
import com.ciclocare.service.LembretesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/lembretes")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LembretesController {

    private final LembretesService lembretesService;

    @PostMapping
    public ResponseEntity<Lembretes> criar(@Valid @RequestBody Lembretes lembretes) {
        log.info("Recebida requisição para criar novo lembrete: {}", lembretes.getTipoProcedimento());
        try {
            Lembretes lembreteSalvo = lembretesService.criar(lembretes);
            log.info("Lembrete criado com sucesso. ID: {}, Tipo: {}", lembreteSalvo.getId(), lembreteSalvo.getTipoProcedimento());
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(lembreteSalvo.getId())
                    .toUri();
            return ResponseEntity.created(location).body(lembreteSalvo);
        } catch (Exception e) {
            log.error("Erro ao criar lembrete: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public List<Lembretes> listar() {
        log.info("Listando todos os lembretes");
        List<Lembretes> lembretes = lembretesService.listar();
        log.debug("Total de filmes encontrados: {}", lembretes.size());
        return lembretes;
    }

    @PutMapping("/{id}")
    public ResponseEntity<LembretesResponse> atualizar(@PathVariable UUID id, @Valid @RequestBody LembretesRequest request) {
        log.info("Atualizando lembrete com ID {}: {}", id, request);
        try {
            LembretesResponse lembreteAtualizado = lembretesService.atualizar(id, request);
            log.debug("Lembrete ID {} atualizado com sucesso", id);
            return ResponseEntity.ok(lembreteAtualizado);
        } catch (Exception e) {
            log.error("Erro ao atualizar lembrete ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        log.info("Excluindo lembrete com ID: {}", id);
        try {
            lembretesService.excluir(id);
            log.debug("Lembrete com ID {} excluído com sucesso", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Erro ao excluir lembrete com ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}


