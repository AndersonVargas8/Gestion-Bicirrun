package com.app.springapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.springapp.dto.EstudianteDTO;
import com.app.springapp.entity.Estudiante;

@Repository
public interface EstudianteRepository extends CrudRepository<Estudiante, Long>{
    public Optional<Estudiante> findByDocumento(String documento);

    @Query(value = "SELECT new com.app.springapp.dto.EstudianteDTO(id, nombres, apellidos) FROM Estudiante ORDER BY nombres")
    public List<EstudianteDTO> findAllIdAndNombres();
}
