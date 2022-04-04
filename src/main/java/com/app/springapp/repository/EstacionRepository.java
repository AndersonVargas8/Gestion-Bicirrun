package com.app.springapp.repository;

import com.app.springapp.entity.Estacion;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstacionRepository extends CrudRepository<Estacion,Long>{
    
}
