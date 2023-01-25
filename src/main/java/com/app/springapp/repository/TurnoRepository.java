package com.app.springapp.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.app.springapp.dto.CuposYTurnosEstacionesHorario;
import com.app.springapp.dto.ReporteTurnosEstudiante;
import com.app.springapp.dto.TurnoDTO;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Horario;
import com.app.springapp.entity.Turno;
import com.app.springapp.util.Mapper;

@Transactional
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

    // Cantidad de turnos en cada dia del mes dado
    public interface ICountTurnosMes {
        long getDia();

        long getCountTurnos();
    }

    @Query(value = "SELECT dia, count(id) AS countTurnos FROM turno WHERE mes = ?1 AND anio = ?2 GROUP BY dia", nativeQuery = true)
    public List<ICountTurnosMes> countAllTurnosByMesAndAnio(int mes, int anio);

    default Map<Integer, Integer> sumAllTurnosByMesAndAnio(int mes, int anio) {
        List<ICountTurnosMes> resultado = countAllTurnosByMesAndAnio(mes, anio);
        Map<Integer, Integer> map = resultado.stream()
                .collect(Collectors.toMap(o -> (int) o.getDia(), o -> (int) o.getCountTurnos()));
        return map;
    }
    /////////////////////////////////////

    // Cantidad de turnos en cada horario en la fecha dada
    public interface ICountTurnosHorario {
        long getIdHorario();

        long getCountTurnos();
    }

    @Query(value = "SELECT horario_id AS idHorario, count(id) AS countTurnos FROM turno WHERE dia = ?1 AND mes = ?2 AND anio = ?3 GROUP BY horario_id", nativeQuery = true)
    public List<ICountTurnosHorario> countAllTurnosByDiaAndMesAndAnio(int dia, int mes, int anio);

    default Map<Long, Integer> sumAllTurnosByDiaAndMesAndAnio(int dia, int mes, int anio) {
        List<ICountTurnosHorario> resultado = countAllTurnosByDiaAndMesAndAnio(dia, mes, anio);
        Map<Long, Integer> map = resultado.stream()
                .collect(Collectors.toMap(o -> o.getIdHorario(), o -> (int) o.getCountTurnos()));
        return map;
    }
    ///////////////////////////

    // Turnos por estacion y horario
    public interface ICountTurnosEstacionAndHorario {
        long getEstacionId();

        long getHorarioId();

        long getSumTurnos();
    }

    @Query(value = "SELECT estacion_id AS estacionId, horario_id AS horarioId, count(id) AS sumTurnos FROM turno WHERE dia = ?1 AND mes = ?2 AND anio = ?3 GROUP BY horario_id, estacion_id", nativeQuery = true)
    public List<ICountTurnosEstacionAndHorario> countTurnosGroupByEstacionHorarioFecha(int dia, int mes, int anio);

    default CuposYTurnosEstacionesHorario sumTurnosGroupByEstacionHorarioFecha(
            CuposYTurnosEstacionesHorario cuposTurnos, int dia, int mes, int anio) {
        List<ICountTurnosEstacionAndHorario> resultado = countTurnosGroupByEstacionHorarioFecha(dia, mes, anio);

        for (ICountTurnosEstacionAndHorario entry : resultado)
            cuposTurnos.putTurno(entry.getEstacionId(), entry.getHorarioId(), (int) entry.getSumTurnos());

        return cuposTurnos;
    };

    public interface ITurnoDTO {
        public long getId();

        public long getDia();

        public long getMes();

        public long getAnio();

        public String getObservaciones();

        public long getEstacionId();

        public String getEstacion();

        public long getEstadoId();

        public String getEstado();

        public long getEstudianteId();

        public String getEstudianteNombres();

        public String getEstudianteApellidos();

        public long getHorarioId();

        public String getHorario();
    }

    @Query(value = "SELECT t.id, dia, mes, anio, observaciones,"
            + " estacion_id AS estacionId, e.nombre AS estacion,"
            + " estado_id AS estadoId, et.descripcion AS estado,"
            + " estudiante_id AS estudianteId, es.nombres AS estudianteNombres, es.apellidos AS estudianteApellidos,"
            + " horario_id AS horarioId, h.descripcion AS horario"
            + " FROM turno t "
            + " INNER JOIN estacion e ON t.estacion_id = e.id"
            + " INNER JOIN estado_turno et ON t.estado_id = et.id"
            + " INNER JOIN estudiante es ON t.estudiante_id = es.id"
            + " INNER JOIN horario h ON t.horario_id = h.id"
            + " WHERE dia = ?1 AND mes = ?2 AND anio = ?3", nativeQuery = true)
    public List<ITurnoDTO> findByDiaAndMesAndAnio(int dia, int mes, int anio);

    default List<TurnoDTO> findByFecha(int dia, int mes, int anio) {
        List<ITurnoDTO> resultado = findByDiaAndMesAndAnio(dia, mes, anio);
        List<TurnoDTO> turnos = new ArrayList<>();

        for (ITurnoDTO turno : resultado) {
            turnos.add(Mapper.mapToTurnoDTOFull(turno));
        }

        return turnos;
    }

    interface ICountProgramados {
        Long getEstudianteId();
        Long getTurnosProgramados();
    }

    
    default  List<ReporteTurnosEstudiante> countProgramadosGroupEstudiante(EntityManager entityManager, String initDate, String finalDate) {
        List<ReporteTurnosEstudiante> resultado= entityManager.createNamedQuery("ReporteTurnosEstudiante")
        .setParameter("initDate", initDate)
        .setParameter("finalDate", finalDate)
        .getResultList();
    
        return resultado;
    }

    default Map<Long, ReporteTurnosEstudiante> countCumplidosGroupEstudiante(EntityManager entityManager, String initDate, String finalDate){
        List<Tuple> resultado = entityManager.createNamedQuery("ReporteTurnosEstado", Tuple.class)
            .setParameter("initDate", initDate)
            .setParameter("finalDate", finalDate)
            .setParameter("estado_id", 2)
            .getResultList();

         
        Map<Long, ReporteTurnosEstudiante> map = resultado.stream()
            .collect(Collectors.toMap(
                o -> Long.valueOf(((String)o.get("documento"))),
                o -> new ReporteTurnosEstudiante(
                    ReporteTurnosEstudiante.TipoTurno.CUMPLIDO,
                    (BigInteger) o.get("countTurnos"),
                    (BigInteger) o.get("sumHoras"))
            ));
        
        return map;
    }

    default Map<Long, ReporteTurnosEstudiante> countIncumplidosGroupEstudiante(EntityManager entityManager, String initDate, String finalDate){
        List<Tuple> resultado = entityManager.createNamedQuery("ReporteTurnosEstado", Tuple.class)
            .setParameter("initDate", initDate)
            .setParameter("finalDate", finalDate)
            .setParameter("estado_id", 3)
            .getResultList();

         
        Map<Long, ReporteTurnosEstudiante> map = resultado.stream()
            .collect(Collectors.toMap(
                o -> Long.valueOf(((String)o.get("documento"))),
                o -> new ReporteTurnosEstudiante(
                    ReporteTurnosEstudiante.TipoTurno.INCUMPLIDO,
                    (BigInteger) o.get("countTurnos"),
                    (BigInteger) o.get("sumHoras"))
            ));
        
        return map;
    }
}
