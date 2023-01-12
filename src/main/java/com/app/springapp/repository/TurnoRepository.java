package com.app.springapp.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    //Cantidad de turnos en cada dia del mes dado
    public interface ICountTurnosMes{
        long getDia();
        long getCountTurnos();
    }
    @Query(value = "SELECT dia, count(id) AS countTurnos FROM turno WHERE mes = ?1 AND anio = ?2 GROUP BY dia", nativeQuery = true)
    public List<ICountTurnosMes> countAllTurnosByMesAndAnio(int mes, int anio);

    default Map<Integer, Integer> sumAllTurnosByMesAndAnio(int mes, int anio){
        List<ICountTurnosMes> resultado = countAllTurnosByMesAndAnio(mes,  anio);
        Map<Integer, Integer> map = resultado.stream().collect(Collectors.toMap(o -> (int)o.getDia(), o -> (int)o.getCountTurnos()));
        return map;
    }
    /////////////////////////////////////

    //Cantidad de turnos en cada horario en la fecha dada
    public interface ICountTurnosHorario{
        long getIdHorario();
        long getCountTurnos();
    }
    @Query(value = "SELECT horario_id AS idHorario, count(id) AS countTurnos FROM turno WHERE dia = ?1 AND mes = ?2 AND anio = ?3 GROUP BY horario_id", nativeQuery = true)
    public List<ICountTurnosHorario> countAllTurnosByDiaAndMesAndAnio(int dia, int mes, int anio);

    default Map<Long, Integer> sumAllTurnosByDiaAndMesAndAnio(int dia, int mes, int anio){
        List<ICountTurnosHorario> resultado = countAllTurnosByDiaAndMesAndAnio(dia, mes,  anio);
        Map<Long, Integer> map = resultado.stream().collect(Collectors.toMap(o -> o.getIdHorario(), o -> (int)o.getCountTurnos()));
        return map;
    }
    ///////////////////////////
}
