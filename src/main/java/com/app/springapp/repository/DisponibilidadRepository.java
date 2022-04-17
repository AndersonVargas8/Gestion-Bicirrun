package com.app.springapp.repository;

import java.util.List;
import java.util.Optional;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Disponibilidad;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisponibilidadRepository extends CrudRepository<Disponibilidad,Integer>{
    public Optional<Disponibilidad> findByMesAndDiaAndCupo(int mes, int dia, Cupo cupo);

    @Query(value = "SELECT SUM(num_disponibles) FROM disponibilidad WHERE dia = ?1 AND mes = ?2 GROUP BY dia,mes", nativeQuery = true)
    public Optional<Integer> cuposDisponiblesEnDia(int dia, int mes);

    @Query(value = "SELECT horario.id FROM disponibilidad d JOIN cupo ON cupo.id = d.cupo_id JOIN horario ON horario.id = cupo.hor_id WHERE d.num_disponibles > 0 AND d.dia = ?1 AND d.mes = ?2 GROUP BY horario.id",nativeQuery = true)
    public List<Integer> idHorariosDisponiblesDiaAndMes(int dia, int mes);
    
    @Query(value = "SELECT horario.id FROM disponibilidad d JOIN cupo ON cupo.id = d.cupo_id JOIN horario ON horario.id = cupo.hor_id WHERE d.num_disponibles > 0 AND d.dia = ?1 AND d.mes = ?2 AND cupo.hor_id <> 4 GROUP BY horario.id",nativeQuery = true)
    public List<Integer> idHorariosDisponiblesDiaAndMesViernes(int dia, int mes);
    
    @Query(value = "SELECT e.id FROM disponibilidad d JOIN cupo c ON (c.id = d.cupo_id OR c.cupo_grupo = d.cupo_id) JOIN horario h ON h.id = c.hor_id JOIN estacion e ON e.id = c.est_id WHERE d.num_disponibles > 0 AND d.dia = ?1 AND d.mes = ?2 AND h.id= ?3 GROUP BY e.id",nativeQuery = true)
    public List<Integer> idEstacionesDisponiblesDiaAndMesAndHorario(int dia, int mes, int idHorario);

    @Query(value = "SELECT d.dia FROM disponibilidad d JOIN cupo c ON (c.id = d.cupo_id OR c.cupo_grupo = d.cupo_id) WHERE d.mes = ?1 AND c.hor_id = ?2 GROUP BY d.dia HAVING SUM(d.num_disponibles) = 0",nativeQuery = true)
    public List<Integer> diasSinDisponibilidadEnHorario(int mes, int idHorario);

}
