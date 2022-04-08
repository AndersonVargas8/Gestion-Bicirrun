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
    public List<Integer> findFreeByDiaAndMes(int dia, int mes);



}
