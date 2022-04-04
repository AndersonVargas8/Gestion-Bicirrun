package com.app.springapp.repository;

import com.app.springapp.entity.Horario;

import org.springframework.stereotype.Repository;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface HorarioRepository extends CrudRepository<Horario,Long>{
    
}
