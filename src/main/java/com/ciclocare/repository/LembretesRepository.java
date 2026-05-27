package com.ciclocare.repository;

import com.ciclocare.entity.Lembretes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LembretesRepository extends JpaRepository<Lembretes, UUID> {

}
