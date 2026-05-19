package com.ciclocare.controller;

import com.ciclocare.model.Alarme;
import com.ciclocare.service.AlarmeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alarmes")
@CrossOrigin("*")
public class AlarmeController {

    @Autowired
    private AlarmeService service;

    @PostMapping("/salvar")
    public Alarme salvar(@RequestBody Alarme alarme) {
        return service.salvar(alarme);
    }

    @GetMapping
    public List<Alarme> listar() {
        return service.listar();
    }

    @PutMapping("/{id}")
    public Alarme atualizar(
            @PathVariable Long id,
            @RequestBody Alarme alarme
    ) {
        return service.atualizar(id, alarme);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
