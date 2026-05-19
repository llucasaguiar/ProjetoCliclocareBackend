package com.ciclocare.controller;

import com.ciclocare.dto.request.ForgotPasswordRequest;
import com.ciclocare.dto.response.ApiResponse;
import com.ciclocare.entity.Usuario;
import com.ciclocare.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class PasswordController {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/recuperar-senha")
    public ResponseEntity<ApiResponse> recuperarSenha(
            @RequestBody
            ForgotPasswordRequest request
    ) {

        Usuario usuario =
                usuarioRepository
                        .findByEmail(request.getEmail())
                        .orElse(null);

        if(usuario == null){

            return ResponseEntity.ok(
                    ApiResponse.sucesso(
                            "Se o email existir, a senha será redefinida."
                    )
            );
        }

        // senha temporária
        String novaSenha =
                UUID.randomUUID()
                        .toString()
                        .substring(0,8);

        usuario.setSenha(
                passwordEncoder.encode(novaSenha)
        );

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(
                ApiResponse.sucesso(
                        "Nova senha temporária: " + novaSenha
                )
        );
    }
}
