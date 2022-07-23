package com.app.springapp.repository;

import java.util.Optional;

import com.app.springapp.entity.Usuario;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Long>{

    public Optional<Usuario> findByUsername(String username);
    
}
