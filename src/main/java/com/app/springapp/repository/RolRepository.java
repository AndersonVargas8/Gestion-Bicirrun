package com.app.springapp.repository;

import com.app.springapp.entity.Rol;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends CrudRepository<Rol,Long>{
    
    public Rol findByNombre(String nombre);
}
