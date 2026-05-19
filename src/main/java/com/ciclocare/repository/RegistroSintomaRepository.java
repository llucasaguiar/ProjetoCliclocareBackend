package com.ciclocare.repository;

import com.ciclocare.entity.RegistroSintoma;
import com.ciclocare.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroSintomaRepository
        extends JpaRepository<RegistroSintoma, Long> {

    List<RegistroSintoma>
    findTop20ByUsuarioOrderByIdDesc(
            Usuario usuario
    );
}
