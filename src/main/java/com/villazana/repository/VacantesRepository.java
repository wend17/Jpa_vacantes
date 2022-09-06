package com.villazana.repository;

import com.villazana.model.Vacante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacantesRepository extends JpaRepository<Vacante,Integer> {

    List<Vacante>findByEstatus(String estatus);
    List<Vacante>findByDestacadoAndEstatusOrderByIdDesc(int destacado,String estatus);

    List<Vacante>findBySalarioBetweenOrderBySalarioDesc(double s1, double s2);

    List<Vacante>findByEstatusIn(String[] estatus);
}
