package com.ciclocare.repository;

import com.ciclocare.model.Alarme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmeRepository extends JpaRepository<Alarme, Long> {
}
