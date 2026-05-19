package com.ciclocare.controller;

import com.ciclocare.dto.request.RegistroSintomaRequest;
import com.ciclocare.entity.RegistroSintoma;
import com.ciclocare.entity.Usuario;
import com.ciclocare.repository.RegistroSintomaRepository;
import com.ciclocare.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/sintomas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RegistroSintomaController {

    private final RegistroSintomaRepository repository;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarRegistro(
            @RequestBody Map<String, Object> dados,
            Authentication authentication
    ) {

        try {

            System.out.println("===== INÍCIO SALVAR =====");

            System.out.println("Dados recebidos:");
            System.out.println(dados);

            System.out.println("Authentication:");
            System.out.println(authentication);

            String email = authentication.getName();

            System.out.println("Email:");
            System.out.println(email);

            Usuario usuario = usuarioRepository
                    .findByEmail(email)
                    .orElseThrow(() ->
                            new RuntimeException("Usuário não encontrado")
                    );

            System.out.println("Usuário encontrado:");
            System.out.println(usuario.getNome());

            List<String> humor =
                    (List<String>) dados.get("humor");

            List<String> sintomas =
                    (List<String>) dados.get("sintomas");

            List<String> sexoLibido =
                    (List<String>) dados.get("sexoLibido");

            List<String> secrecao =
                    (List<String>) dados.get("secrecao");

            String observacao =
                    (String) dados.get("observacao");

            System.out.println("Salvando humor...");
            salvarLista(humor, "HUMOR", usuario);

            System.out.println("Salvando sintomas...");
            salvarLista(sintomas, "SINTOMAS", usuario);

            System.out.println("Salvando sexo/libido...");
            salvarLista(sexoLibido, "SEXO_LIBIDO", usuario);

            System.out.println("Salvando secreção...");
            salvarLista(secrecao, "SECRECAO", usuario);

            if (
                    observacao != null &&
                            !observacao.isBlank()
            ) {

                System.out.println("Salvando observação...");

                RegistroSintoma registro =
                        RegistroSintoma.builder()
                                .categoria("OBSERVACAO")
                                .descricao(observacao)
                                .usuario(usuario)
                                .dataRegistro(LocalDateTime.now())
                                .build();

                repository.save(registro);
            }

            System.out.println("===== SALVOU COM SUCESSO =====");

            return ResponseEntity.ok(
                    Map.of(
                            "sucesso", true,
                            "mensagem", "Registro salvo com sucesso"
                    )
            );

        } catch (Exception e) {

            System.out.println("===== ERRO NO SALVAR =====");

            e.printStackTrace();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "sucesso", false,
                                    "erro", e.getMessage()
                            )
                    );
        }
    }

    @GetMapping("/ultimo")
    public ResponseEntity<?> ultimoRegistro(
            Authentication authentication
    ) {

        try {
            if (authentication == null) {

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(
                                Map.of(
                                        "sucesso", false,
                                        "mensagem", "Usuário não autenticado"
                                )
                        );
            }

            String email = authentication.getName();

            Usuario usuario = usuarioRepository
                    .findByEmail(email)
                    .orElse(null);

            if (usuario == null) {

                Map<String, Object> vazio =
                        new HashMap<>();

                vazio.put("humor", List.of());
                vazio.put("sintomas", List.of());
                vazio.put("sexoLibido", List.of());
                vazio.put("secrecao", List.of());
                vazio.put("observacao", "");

                return ResponseEntity.ok(vazio);
            }

            List<RegistroSintoma> registros =
                    repository.findTop20ByUsuarioOrderByIdDesc(
                            usuario
                    );

            if (
                    registros == null ||
                            registros.isEmpty()
            ) {

                Map<String, Object> vazio =
                        new HashMap<>();

                vazio.put("humor", List.of());
                vazio.put("sintomas", List.of());
                vazio.put("sexoLibido", List.of());
                vazio.put("secrecao", List.of());
                vazio.put("observacao", "");

                return ResponseEntity.ok(vazio);
            }

            List<String> humor =
                    new ArrayList<>();

            List<String> sintomas =
                    new ArrayList<>();

            List<String> sexoLibido =
                    new ArrayList<>();

            List<String> secrecao =
                    new ArrayList<>();

            String observacao = "";

            for (RegistroSintoma r : registros) {

                if (r.getCategoria() == null) {
                    continue;
                }

                switch (r.getCategoria()) {

                    case "HUMOR":

                        humor.add(
                                r.getDescricao()
                        );

                        break;

                    case "SINTOMAS":

                        sintomas.add(
                                r.getDescricao()
                        );

                        break;

                    case "SEXO_LIBIDO":

                        sexoLibido.add(
                                r.getDescricao()
                        );

                        break;

                    case "SECRECAO":

                        secrecao.add(
                                r.getDescricao()
                        );

                        break;

                    case "OBSERVACAO":

                        observacao =
                                r.getDescricao();

                        break;
                }
            }

            Map<String, Object> resposta =
                    new HashMap<>();

            resposta.put("humor", humor);

            resposta.put("sintomas", sintomas);

            resposta.put(
                    "sexoLibido",
                    sexoLibido
            );

            resposta.put(
                    "secrecao",
                    secrecao
            );

            resposta.put(
                    "observacao",
                    observacao
            );

            return ResponseEntity.ok(resposta);

        } catch (Exception e) {

            e.printStackTrace();

            Map<String, Object> erro =
                    new HashMap<>();

            erro.put("erro", true);

            erro.put(
                    "mensagem",
                    e.getMessage() != null
                            ? e.getMessage()
                            : "Erro interno no servidor"
            );

            return ResponseEntity
                    .status(
                            HttpStatus
                                    .INTERNAL_SERVER_ERROR
                    )
                    .body(erro);
        }
    }

    private void salvarLista(
            List<String> lista,
            String categoria,
            Usuario usuario
    ) {

        if (lista == null || lista.isEmpty()) {
            return;
        }

        for (String item : lista) {

            RegistroSintoma registro =
                    RegistroSintoma.builder()
                            .categoria(categoria)
                            .descricao(item)
                            .usuario(usuario)
                            .dataRegistro(LocalDateTime.now())
                            .build();

            repository.save(registro);
        }
    }
}