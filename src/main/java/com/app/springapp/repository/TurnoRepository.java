package com.app.springapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Horario;
import com.app.springapp.entity.Turno;

@Transactional
@Repository
public interface TurnoRepository extends CrudRepository<Turno, Long> {

    public List<Turno> findByDiaAndMes(int dia, int mes);

    public List<Turno> findByEstudiante(Estudiante estudiante);

    public List<Turno> findByDiaAndMesAndAnioAndEstacion(int dia, int mes, int anio, Estacion estacion);

    public List<Turno> findByDiaAndMesAndAnioAndEstacionAndHorario(int dia, int mes, int anio, Estacion estacion,
            Horario horario);

    public long countByDiaAndMesAndAnio(int dia, int mes, int anio);

    public long countByDiaAndMesAndAnioAndEstacionAndHorario(int dia, int mes, int anio, Estacion estacion,
            Horario horario);

    public long countByDiaAndMesAndAnioAndHorarioAndEstudiante(int dia, int mes, int anio, Horario horario,
            Estudiante estudiante);

    public long countByDiaAndMesAndAnioAndHorario(int dia, int mes, int anio, Horario horario);

    @Query(value = "SELECT DISTINCT dia, mes, anio FROM turno WHERE (mes > ?2 AND anio >= ?3) OR (dia >= ?1 AND mes = ?2 AND anio = ?3)", nativeQuery = true)
    public List<Object[]> findDistinctPostFecha(int dia, int mes, int anio);

    @Query(value = "SELECT DISTINCT dia, mes, anio FROM turno WHERE ((mes > ?2 AND anio >= ?3) OR (dia >= ?1 AND mes = ?2 AND anio = ?3)) AND horario_id = ?4", nativeQuery = true)
    public List<Object[]> findDistinctByHorarioPostFecha(int dia, int mes, int anio, int idHorario);


    @Modifying
    @Query("update Turno t set t.id = :nuevoId where t.id = :actualId")
    public void updateId(@Param("actualId") Long actualId, @Param("nuevoId") Long nuevoId);

}
