package com.ciclocare.controller;

import com.ciclocare.dto.response.ApiResponse;
import com.ciclocare.entity.Usuario;
import com.ciclocare.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public ApiResponse buscarDashboard() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email =
                authentication.getName();

        Usuario usuario =
                usuarioRepository
                        .findByEmail(email)
                        .orElseThrow();

        return ApiResponse.sucesso(
                "Dashboard carregada",
                usuario
        );
    }
}
