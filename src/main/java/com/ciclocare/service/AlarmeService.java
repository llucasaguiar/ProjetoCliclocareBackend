package com.ciclocare.service;

import com.ciclocare.model.Alarme;
import com.ciclocare.repository.AlarmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmeService {

    @Autowired
    private AlarmeRepository repository;

    public Alarme salvar(Alarme alarme) {
        return repository.save(alarme);
    }

    public List<Alarme> listar() {
        return repository.findAll();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Alarme atualizar(Long id, Alarme novoAlarme) {

        Alarme alarme = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alarme não encontrado"));

        alarme.setTipo(novoAlarme.getTipo());
        alarme.setNome(novoAlarme.getNome());
        alarme.setFrequencia(novoAlarme.getFrequencia());
        alarme.setHoraInicio(novoAlarme.getHoraInicio());
        alarme.setObservacao(novoAlarme.getObservacao());
        alarme.setAtivo(novoAlarme.isAtivo());

        return repository.save(alarme);
    }
}