package com.app.springapp.repository;

import java.util.List;
import java.util.Optional;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
}
