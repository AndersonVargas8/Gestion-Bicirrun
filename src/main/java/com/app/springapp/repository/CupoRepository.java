package com.app.springapp.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.springapp.dto.CountCuposDTO;
import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;

@Repository
public interface CupoRepository extends CrudRepository<Cupo,Integer>{
    @Query(value = "SELECT c FROM Cupo c WHERE c.horario.id <> 4")
    public List<Cupo> findAllViernes();

    public Optional<Cupo> findByEstacionAndHorario(Estacion estacion, Horario horario);

    public Cupo findById(Long id);

    public List<Cupo> findByHorario(Horario horario);

    @Query(value = "SELECT e.id FROM cupo c JOIN estacion e ON c.est_id = e.id WHERE c.hor_id = ?1", nativeQuery = true)
    public List<Integer> idEstacionesPorHorario(Long idHorario);

    
    @Query("SELECT sum(c.num_cupos) FROM Cupo c WHERE c.horario = ?1")
    public long sumCuposByHorario(Horario horario);

    @Query(value = "SELECT SUM(num_cupos) FROM Cupo")
    public long sumTotalCupos();

    @Query(value = "SELECT new com.app.springapp.dto.CountCuposDTO(horario.id, sum(num_cupos)) FROM Cupo GROUP BY hor_id") 
    public List<CountCuposDTO> countCuposGroupByHorario();
        
    default Map<Long, Integer> sumCuposGroupByHorario(){
        List<CountCuposDTO> resultado = countCuposGroupByHorario();
        Map<Long, Integer> map = resultado.stream().collect(Collectors.toMap(o -> o.horarioId, o -> o.sumCupos));
        return map;
    };
}
