package com.villazana.repository;

import com.villazana.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepository extends JpaRepository<Usuario,Integer> {
}
