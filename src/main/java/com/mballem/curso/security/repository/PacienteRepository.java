package com.mballem.curso.security.repository;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente,Long> {

        @Query("select p from Paciente p where p.usuario.email like :email")
        Optional<Paciente> findByUsuarioEmail(String email);
}
