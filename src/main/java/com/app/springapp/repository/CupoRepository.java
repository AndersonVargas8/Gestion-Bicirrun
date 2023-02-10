package com.app.springapp.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.springapp.dto.CountCuposDTO;
import com.app.springapp.dto.CuposYTurnosEstacionesHorario;
import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;

@Repository
public interface CupoRepository extends CrudRepository<Cupo,Integer>{
    @Query(value = "SELECT c FROM Cupo c WHERE c.horario.id <> 4")
    public List<Cupo> findAllViernes();

    public Optional<Cupo> findByEstacionAndHorario(Estacion estacion, Horario horario);

    public Cupo findById(Long id);

    @Query(value = "SELECT c FROM Cupo c WHERE c.horario = ?1 AND c.estacion.isHabilitada = true")
    public List<Cupo> findByHorario(Horario horario);

    @Query(value = "SELECT e.id FROM cupo c JOIN estacion e ON c.est_id = e.id WHERE c.hor_id = ?1", nativeQuery = true)
    public List<Integer> idEstacionesPorHorario(Long idHorario);

    
    @Query("SELECT sum(c.num_cupos) FROM Cupo c WHERE c.horario = ?1 AND c.estacion.isHabilitada = true")
    public long sumCuposByHorario(Horario horario);

    @Query(value = "SELECT SUM(c.num_cupos) FROM Cupo c WHERE c.estacion.isHabilitada = true")
    public long sumTotalCupos();

        //Cupos por horario
    @Query(value = "SELECT new com.app.springapp.dto.CountCuposDTO(horario.id, sum(num_cupos)) FROM Cupo WHERE estacion.isHabilitada = true  GROUP BY hor_id") 
    public List<CountCuposDTO> countCuposGroupByHorario();
        
    default Map<Long, Integer> sumCuposGroupByHorario(){
        List<CountCuposDTO> resultado = countCuposGroupByHorario();
        Map<Long, Integer> map = resultado.stream().collect(Collectors.toMap(o -> o.horarioId, o -> o.sumCupos));
        return map;
    };

    //Cupos por estacion y horario
    public interface ICountCuposEstacionAndHorario{
        long getEstacionId();
        long getHorarioId();
        long getSumCupos();
    }
    @Query(value = "SELECT est_id AS estacionId, hor_id AS horarioId, sum(num_cupos) AS sumCupos FROM cupo INNER JOIN estacion e ON est_id = e.id WHERE e.is_habilitada = true GROUP BY hor_id, est_id", nativeQuery = true) 
    public List<ICountCuposEstacionAndHorario> countCuposGroupByEstacionHorario();
    
    default CuposYTurnosEstacionesHorario sumCuposGroupByEstacionHorario(CuposYTurnosEstacionesHorario cuposTurnos){
        List<ICountCuposEstacionAndHorario> resultado = countCuposGroupByEstacionHorario();

        for(ICountCuposEstacionAndHorario entry: resultado)
            cuposTurnos.putCupo(entry.getEstacionId(), entry.getHorarioId(), (int)entry.getSumCupos());

        return cuposTurnos;
    };
}
