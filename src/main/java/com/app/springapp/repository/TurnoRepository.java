package com.app.springapp.repository;

import com.app.springapp.entity.Turno;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepository extends CrudRepository<Turno, Long>{
    
}
