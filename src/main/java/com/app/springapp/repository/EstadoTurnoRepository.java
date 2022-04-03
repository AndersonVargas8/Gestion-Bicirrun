package com.app.springapp.repository;

import com.app.springapp.entity.EstadoTurno;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoTurnoRepository extends CrudRepository<EstadoTurno,Long>{
    
}
