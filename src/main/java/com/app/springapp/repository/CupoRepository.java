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

    public Cupo findByHorario(Horario horario);

   /*  public Optional<Cupo> findByCupoGrupo(int cupoGrupo);

     @Query(value = "SELECT c.id FROM cupo c JOIN cupo cg ON cg.id = c.cupo_grupo WHERE c.cupo_grupo <> 0 AND cg.hor_id = ?1 GROUP BY c.hor_id;", nativeQuery = true)//DESARROLLO
    @Query(value = "SELECT DISTINCT ON (c.hor_id) c.id FROM cupo c JOIN cupo cg ON cg.id = c.cupo_grupo WHERE c.cupo_grupo <> 0 AND cg.hor_id = ?1 GROUP BY c.id,c.hor_id", nativeQuery = true)//PRODUCCIÓN
    public Optional<Integer> findIdByCupoGrupoHorario(int idHorario);

    @Query(value = "SELECT SUM(c.num_cupos) FROM cupo c WHERE c.cupo_grupo = 0",nativeQuery = true)
    public Integer sumNumCupos();

    @Query(value = "SELECT SUM(c.num_cupos) FROM cupo c WHERE c.cupo_grupo = 0 AND c.hor_id <> 4",nativeQuery = true)
    public Integer sumNumCuposViernes();
*/
    @Query(value = "SELECT e.id FROM cupo c JOIN estacion e ON c.est_id = e.id WHERE c.hor_id = ?1", nativeQuery = true)
    public List<Integer> idEstacionesPorHorario(Long idHorario);
}
