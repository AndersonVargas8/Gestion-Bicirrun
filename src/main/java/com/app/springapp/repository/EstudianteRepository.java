package com.app.springapp.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.springapp.entity.Estudiante;

@Repository
public interface EstudianteRepository extends CrudRepository<Estudiante, Long>{
    public Optional<Estudiante> findByDocumento(String documento);
}
