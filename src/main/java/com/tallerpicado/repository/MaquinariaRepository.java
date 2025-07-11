package com.tallerpicado.repository;

import com.tallerpicado.domain.Maquinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaquinariaRepository extends JpaRepository<Maquinaria, Long> {
}
