package com.app.springapp.repository;

import java.util.List;

import com.app.springapp.entity.Estudiante;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends CrudRepository<Estudiante, Long>{
}
