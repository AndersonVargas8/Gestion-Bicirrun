package com.app.springapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.springapp.entity.Estacion;

@Transactional
@Repository
public interface EstacionRepository extends CrudRepository<Estacion,Long>{
    @Query(value = "SELECT e FROM Estacion e WHERE isHabilitada = true ORDER BY id ASC")
    public List<Estacion> findAllByOrderByIdAsc();

    @Modifying
    @Query(value = "UPDATE estacion SET is_habilitada = ?2 WHERE id = ?1", nativeQuery = true)
    public void updateIsHabilitada(int id, boolean isHabilitada);

    public List<Estacion> findAllByOrderByNombreAsc();
}
