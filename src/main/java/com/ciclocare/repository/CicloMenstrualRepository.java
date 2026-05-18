package com.ciclocare.repository;

import com.ciclocare.entity.CicloMenstrual;
import com.ciclocare.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CicloMenstrualRepository extends JpaRepository<CicloMenstrual, UUID> {

    @Query("SELECT c FROM CicloMenstrual c WHERE c.usuario = :usuario ORDER BY c.criadoEm DESC LIMIT 1")
    Optional<CicloMenstrual> findUltimoByUsuario(Usuario usuario);

    Optional<CicloMenstrual> findByUsuarioOrderByDataInicioDesc(Usuario usuario);

    @Query("SELECT c FROM CicloMenstrual c WHERE c.usuario = :usuario ORDER BY c.dataInicio DESC")
    List<CicloMenstrual> findAllByUsuario(Usuario usuario);
}
