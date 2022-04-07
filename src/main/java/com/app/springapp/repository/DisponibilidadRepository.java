package com.app.springapp.repository;

import java.util.Optional;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Disponibilidad;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibilidadRepository extends CrudRepository<Disponibilidad,Integer>{
    public Optional<Disponibilidad> findByMesAndDiaAndCupo(int mes, int dia, Cupo cupo);
}
