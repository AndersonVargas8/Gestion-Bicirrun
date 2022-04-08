package com.app.springapp.repository;

import java.util.Optional;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CupoRepository extends CrudRepository<Cupo,Integer>{
    public Optional<Cupo> findByEstacionAndHorario(Estacion estacion, Horario horario);

    public Cupo findById(Long id);

    public Cupo findByHorario(Horario horario);

    public Optional<Cupo> findByCupoGrupo(int cupoGrupo);

    @Query(value = "SELECT c.id FROM cupo c JOIN cupo cg ON cg.id = c.cupo_grupo WHERE c.cupo_grupo <> 0 AND cg.hor_id = ?1 GROUP BY c.hor_id;", nativeQuery = true)
    public Optional<Integer> findIdByCupoGrupoHorario(int idHorario);
}
