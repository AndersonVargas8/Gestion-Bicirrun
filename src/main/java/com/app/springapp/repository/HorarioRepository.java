package com.app.springapp.repository;

import java.util.List;

import org.json.JSONArray;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.springapp.entity.Horario;

@Repository
public interface HorarioRepository extends CrudRepository<Horario,Long>{
    
    public interface IHorariosDiasNoDisponibles{
        Long getHorarioId();
        String getDia();
    }
    @Query(value = "SELECT horario_id AS horarioId, dias_no_disponibles AS dia FROM horario_dias_no_disponibles", nativeQuery = true)
    public List<IHorariosDiasNoDisponibles> findHorariosDiasNoDisponibles();
}
