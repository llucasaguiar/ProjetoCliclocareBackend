package com.ciclocare.controller;

import com.ciclocare.dto.request.RegisterRequest;
import com.ciclocare.dto.request.UpdateProfileRequest;
import com.ciclocare.dto.response.ApiResponse;
import com.ciclocare.entity.Usuario;
import com.ciclocare.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

	@PostMapping("/cadastro1")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ApiResponse> cadastrar(
			@Valid @RequestBody RegisterRequest registerRequest
			) 	{
		try {
			var usuaria = usuarioService.registrar(registerRequest);

			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(ApiResponse.sucesso(
							"Usuária cadastrada com sucesso!", usuaria
					));
		} catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponse.erro(e.getMessage()));
		}
	}

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> buscarPorId(@PathVariable UUID id) {
        try {
            var usuario = usuarioService.buscarPorId(id);
            return ResponseEntity.ok(ApiResponse.sucesso("Usuário encontrado", usuario));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @GetMapping("/perfil/meu")
    public ResponseEntity<ApiResponse> meuPerfil() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            var usuario = usuarioService.buscarPorEmail(email);
            return ResponseEntity.ok(ApiResponse.sucesso("Perfil recuperado", usuarioService.mapToResponse(usuario)));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProfileRequest request) {
        try {
            var usuarioAtualizado = usuarioService.atualizarPerfil(id, request);
            return ResponseEntity.ok(ApiResponse.sucesso("Perfil atualizado com sucesso", usuarioAtualizado));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletar(@PathVariable UUID id) {
        try {
            usuarioService.deletarConta(id);
            return ResponseEntity.ok(ApiResponse.sucesso("Conta deletada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.erro(e.getMessage()));
        }
    }
}
