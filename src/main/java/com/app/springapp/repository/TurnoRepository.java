package com.app.springapp.repository;

import java.util.List;

import com.app.springapp.entity.Turno;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepository extends CrudRepository<Turno, Long>{
    public List<Turno> findByDiaAndMes(int dia, int mes);
}
