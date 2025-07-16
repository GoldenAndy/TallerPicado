package com.tallerpicado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tallerpicado.domain.Maquinaria;

@Repository
public interface MaquinariaRepository extends JpaRepository<Maquinaria, Long> {
}
