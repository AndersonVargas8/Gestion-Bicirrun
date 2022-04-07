package com.app.springapp.repository;

import java.util.Optional;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupoRepository extends CrudRepository<Cupo,Integer>{
    public Optional<Cupo> findByEstacionAndHorario(Estacion estacion, Horario horario);

    public Cupo findById(Long id);
}
