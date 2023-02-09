package com.app.springapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.springapp.entity.Estacion;

@Repository
public interface EstacionRepository extends CrudRepository<Estacion,Long>{
    @Query(value = "SELECT e FROM Estacion e WHERE isHabilitada = true ORDER BY id ASC")
    public List<Estacion> findAllByOrderByIdAsc();
}
