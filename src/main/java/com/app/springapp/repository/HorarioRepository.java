package com.app.springapp.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // Horarios dependientes e independientes
    public interface IHorariosDependientes{
        long getHorarioDependiente();
        long getHorarioIndependiente();
    }
    @Query(value = "SELECT DISTINCT tmp1.horarioDependiente, tmp2.horarioIndependiente FROM "
    +"("
        +"SELECT cc.cupo_dependiente AS id,hor_id AS horarioDependiente " 
        +"FROM cupo c " 
        +"INNER JOIN cupos_compartidos cc ON c.id = cc.cupo_dependiente "
    +")tmp1 "
    +"INNER JOIN "
    +"("
       +"SELECT cc.cupo_dependiente AS id, hor_id AS horarioIndependiente " 
        +"FROM cupo c " 
        +"INNER JOIN cupos_compartidos cc ON c.id = cc.cupo_independiente "
    +")tmp2 "
    +"ON tmp1.id = tmp2.Id"
        , nativeQuery = true)
    public List<IHorariosDependientes> turnosDependientes();
    
    default Map<Long, Long> findTurnosDependientes(){
        List<IHorariosDependientes> resultado = turnosDependientes();
        Map<Long, Long> map = resultado.stream().collect(Collectors.toMap(o -> o.getHorarioDependiente(), o -> o.getHorarioIndependiente()));
        return map;
    }
}
