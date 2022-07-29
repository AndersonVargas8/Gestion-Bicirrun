package com.app.springapp.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.springapp.dto.Calendario;
import com.app.springapp.dto.TurnoDTO;
import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Horario;
import com.app.springapp.entity.Turno;
import com.app.springapp.interfacesServicios.IServicioCupo;
import com.app.springapp.interfacesServicios.IServicioEstacion;
import com.app.springapp.interfacesServicios.IServicioEstudiante;
import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.interfacesServicios.IServicioTurno;
import com.app.springapp.model.Mes;
import com.app.springapp.repository.EstadoTurnoRepository;
import com.app.springapp.repository.TurnoRepository;
import com.app.springapp.util.Mapper;

@Service
public class TurnoService implements IServicioTurno {
    @Autowired
    TurnoRepository repTurno;

    @Autowired
    IServicioCupo serCupo;

    @Autowired
    IServicioEstacion serEstacion;

    @Autowired
    IServicioEstudiante serEstudiante;

    @Autowired
    IServicioHorario serHorario;

    @Autowired
    EstadoTurnoRepository repEstadoTurno;

    @Override
    public Turno obtenerPorId(long id) {
        return repTurno.findById(id).get();
    }

    @Override
    public List<Turno> obtenerTodos() {
        return (List<Turno>) repTurno.findAll();
    }

    @Override
    public List<Turno> obtenerPorDiaMes(int dia, int mes) {
        List<Turno> respuesta = repTurno.findByDiaAndMes(dia, mes);
        if (respuesta.isEmpty())
            return null;

        return respuesta;
    }

    /**
     * Retorna la cantidad de turnos programados en la misma fecha, estación y
     * horario del turno proporcionado
     * 
     * @param turno
     * @return Número de turnos programados
     */
    public long cantidadTurnosProgramados(Turno turno) {
        Horario horario = turno.getHorario();

        Cupo cupo = null;
        try {
            cupo = serCupo.buscarPorEstacionYHorario(turno.getEstacion(), turno.getHorario());
        } catch (Exception e) {
            throw new IllegalArgumentException("El horario no está disponible para la estación proporcionada");
        }

        long numeroTurnos = repTurno.countByDiaAndMesAndAnioAndEstacionAndHorario(turno.getDia(), turno.getMes(),
                turno.getAnio(), turno.getEstacion(), horario);

        if (!cupo.getCupos_independientes().isEmpty()) {
            horario = cupo.getCupos_independientes().get(0).getHorario();
            numeroTurnos += repTurno.countByDiaAndMesAndAnioAndEstacionAndHorario(turno.getDia(), turno.getMes(),
                    turno.getAnio(), turno.getEstacion(), horario);
        }

        if (!cupo.getCupos_dependientes().isEmpty()) {
            horario = cupo.getCupos_dependientes().get(0).getHorario();
            numeroTurnos += repTurno.countByDiaAndMesAndAnioAndEstacionAndHorario(turno.getDia(), turno.getMes(),
                    turno.getAnio(), turno.getEstacion(), horario);
        }

        return numeroTurnos;
    }

    /**
     * Verifica si existe disponibilidad para crear un turno con la fecha, estación
     * y horario que contiene.
     * 
     * @param turno
     * @return True si está disponible. False en otro caso
     */
    public boolean turnoEstaDisponible(Turno turno) {
        LocalDate fecha = LocalDate.of(turno.getAnio(), turno.getMes(), turno.getDia());
        int valorDiaSemana = fecha.get(WeekFields.ISO.dayOfWeek());
        String nombreDia = Calendario.convertirNumeroADia(valorDiaSemana).toLowerCase();
        if (turno.getHorario().diaNoDisponible(nombreDia)) {
            return false;
        }

        Cupo cupo = null;
        try {
            cupo = serCupo.buscarPorEstacionYHorario(turno.getEstacion(), turno.getHorario());
        } catch (Exception e) {
            throw new IllegalArgumentException("El horario no está disponible para la estación proporcionada");
        }

        int numeroCupos = serCupo.obtenerNumeroCupos(cupo);
        long numeroTurnos = cantidadTurnosProgramados(turno);

        if (numeroTurnos < numeroCupos)
            return true;
        return false;
    }

    /**
     * Verifica si el estudiante tiene programado algún turno en el horario y fecha
     * del turno especificado
     * 
     * @param turno
     * @return True si el estudiante no tiene un turno programado en esa fecha y
     *         horario. False en otro caso.
     */
    public boolean estudianteEstaDisponible(Turno turno) {
        Horario horario = turno.getHorario();
        long numeroTurnos = repTurno.countByDiaAndMesAndAnioAndHorarioAndEstudiante(turno.getDia(), turno.getMes(),
                turno.getAnio(), turno.getHorario(), turno.getEstudiante());

        if (numeroTurnos > 0)
            return false;

        List<Cupo> cupos = serCupo.buscarPorHorario(horario);

        for (Cupo cupo : cupos) {
            if (!cupo.getCupos_independientes().isEmpty()) {
                horario = cupo.getCupos_independientes().get(0).getHorario();
                numeroTurnos += repTurno.countByDiaAndMesAndAnioAndHorarioAndEstudiante(turno.getDia(), turno.getMes(),
                        turno.getAnio(), horario, turno.getEstudiante());
                break;
            }
            if (!cupo.getCupos_dependientes().isEmpty()) {
                horario = cupo.getCupos_dependientes().get(0).getHorario();
                numeroTurnos += repTurno.countByDiaAndMesAndAnioAndHorarioAndEstudiante(turno.getDia(), turno.getMes(),
                        turno.getAnio(), horario, turno.getEstudiante());
                break;
            }
        }

        if (numeroTurnos > 0)
            return false;

        return true;
    }

    private void actualizarArchivosAgregarTurno(Turno turno) {
        int turnosProgramados = repTurno.agregarTurnoProgramado(turno.getDia(), turno.getMes(), turno.getAnio());
        int cantidadCupos = serCupo.cantidadCuposPorFecha(turno.getDia(), turno.getMes(), turno.getAnio());

        if (turnosProgramados >= cantidadCupos) {
            repTurno.agregarDiaTurnosCompletos(turno.getDia(), turno.getMes(), turno.getAnio());
        }
    }

    private void actualizarArchivosEliminarTurno(Turno turno) {
        int dia = turno.getDia(), mes = turno.getMes(), anio = turno.getAnio();
        repTurno.eliminarTurnoProgramado(dia, mes, anio);
        repTurno.eliminarDiaTurnosCompletos(dia, mes, anio);
    }

    @Override
    public TurnoDTO guardarTurno(TurnoDTO turnoDTO) {
        Turno turno = Mapper.mapToTurno(turnoDTO, serEstacion, serEstudiante, serHorario, repEstadoTurno);

        // Validar dia disponible
        if (!turnoEstaDisponible(turno)) {
            throw new IllegalArgumentException("No hay turnos disponibles en la fecha, horario y estación indicados");
        }

        // Validar estudiante disponibnle
        if (!estudianteEstaDisponible(turno)) {
            throw new IllegalArgumentException("El estudiante ya tiene un turno programado en el mismo horario");
        }

        repTurno.save(turno);

        actualizarArchivosAgregarTurno(turno);

        turnoDTO = Mapper.mapToTurnoDTO(turno);
        return turnoDTO;
    }

    @Override
    public void eliminarTurno(int idTurno) {
        if (!repTurno.existsById(new Long(idTurno))) {
            throw new IllegalArgumentException("No existe un turno con el id proporcionado");
        }
        Turno turno = obtenerPorId(idTurno);
        actualizarArchivosEliminarTurno(turno);
        repTurno.deleteById(new Long(idTurno));
    }

    @Override
    public TurnoDTO editarTurno(int idTurno, TurnoDTO turnoDTO) {
        Turno turnoActual = new Turno();
        boolean crearNuevo = true;
        if (repTurno.existsById(new Long(idTurno))) {
            turnoActual = obtenerPorId(idTurno).clone();
            crearNuevo = false;
        }

        Turno turno = Mapper.mapToTurno(turnoDTO, serEstacion, serEstudiante, serHorario, repEstadoTurno);
        turno.setId(idTurno);

        if (crearNuevo || turnoActual.compareTo(turno) != 0 || !turnoActual.getEstacion().equals(turno.getEstacion())
                || !turnoActual.getHorario().equals(turno.getHorario())) {
            // Validar dia disponible
            if (!turnoEstaDisponible(turno)) {
                throw new IllegalArgumentException(
                        "No hay turnos disponibles en la fecha, horario y estación indicados");
            }
        }

        if (crearNuevo || !turnoActual.getEstudiante().equals(turno.getEstudiante())) {
            // Validar estudiante disponibnle
            if (!estudianteEstaDisponible(turno)) {
                throw new IllegalArgumentException("El estudiante ya tiene un turno programado en el mismo horario");
            }
        }

        turno = repTurno.save(turno);

        if (crearNuevo) {
            repTurno.updateId(turno.getId(), new Long(idTurno));
            actualizarArchivosAgregarTurno(turno);
        } else if (turnoActual.compareTo(turno) != 0) {
            actualizarArchivosEliminarTurno(turnoActual);
            actualizarArchivosAgregarTurno(turno);
        }

        return Mapper.mapToTurnoDTO(turno);

    }

    @Override
    public TurnoDTO parcharTurno(int idTurno, TurnoDTO turnoDTO) {
        if (!repTurno.existsById(new Long(idTurno))) {
            throw new IllegalArgumentException("No existe un turno con el id proporcionado");
        }

        Turno turno = obtenerPorId(idTurno).clone();
        Turno turnoActual = turno.clone();

        if (turnoDTO.fecha != null) {
            if (Mapper.esFechaValida(turnoDTO.fecha)) {
                String[] fecha = turnoDTO.fecha.split("-");
                turno.setDia(Integer.parseInt(fecha[0]));
                turno.setMes(Integer.parseInt(fecha[1]));
                turno.setAnio(Integer.parseInt(fecha[2]));
            }
        }

        if (turnoDTO.observaciones != null) {
            turno.setObservaciones(turnoDTO.observaciones);
        }

        if (turnoDTO.idEstacion != 0) {
            turno.setEstacion(serEstacion.buscarPorId(turnoDTO.idEstacion));
        }

        if (turnoDTO.idEstado != 0) {
            turno.setEstado(repEstadoTurno.findById(new Long(turnoDTO.idEstado)).get());
        }

        if (turnoDTO.idEstudiante != 0) {
            turno.setEstudiante(serEstudiante.buscarPorId(new Long(turnoDTO.idEstudiante)));
        }

        if (turnoDTO.idHorario != 0) {
            turno.setHorario(serHorario.buscarPorId(turnoDTO.idHorario));
        }

        if (turnoActual.compareTo(turno) != 0 || !turnoActual.getEstacion().equals(turno.getEstacion())
                || !turnoActual.getHorario().equals(turno.getHorario())) {
            // Validar dia disponible
            if (!turnoEstaDisponible(turno)) {
                throw new IllegalArgumentException(
                        "No hay turnos disponibles en la fecha, horario y estación indicados");
            }
        }

        if (!turnoActual.getEstudiante().equals(turno.getEstudiante())) {
            // Validar estudiante disponibnle
            if (!estudianteEstaDisponible(turno)) {
                throw new IllegalArgumentException("El estudiante ya tiene un turno programado en el mismo horario");
            }
        }

        turno = repTurno.save(turno);

        if (turnoActual.compareTo(turno) != 0) {
            actualizarArchivosEliminarTurno(turnoActual);
            actualizarArchivosAgregarTurno(turno);
        }
        return Mapper.mapToTurnoDTO(turno);
    }

    @Override
    public List<Turno> obtenerPorEstudiante(Estudiante estudiante) {
        List<Turno> respuesta = repTurno.findByEstudiante(estudiante);
        Collections.sort(respuesta);
        return respuesta;
    }

    /**
     * @param mes
     * @param anio
     * @return
     */
    public Calendario getCalendario(int mes, int anio) {
        // Obtener la fecha con el mes y año dado
        LocalDate fecha = LocalDate.of(anio, mes, 1);
        int diasDelMes = fecha.lengthOfMonth();

        Calendario calendario = new Calendario(mes, anio);

        // Objeto Mes para buscar los turnos programados
        Mes mesTurnosPro = repTurno.getMesTurnosProgramados(anio, calendario.mes);

        // HashMap con los cupos por día
        HashMap<String, Integer> cuposPorDia = serCupo.cantidadCuposAlDia();

        int numeroSemanasGuardadas = 0;

        for (int diaMes = 1; diaMes <= diasDelMes; diaMes++) {
            LocalDate dia = fecha.withDayOfMonth(diaMes);
            int valorDiaActual = DayOfWeek.from(dia).getValue();
            String nombreDia = Calendario.convertirNumeroADia(valorDiaActual);

            if (valorDiaActual <= 5) {// Si el día no es sábado ni domingo
                int cuposDia = cuposPorDia.get(nombreDia);
                int turnosProgramados = mesTurnosPro.numeroTurnosDia(diaMes);
                calendario.agregarDiaSemana(numeroSemanasGuardadas, valorDiaActual, diaMes, cuposDia,
                        turnosProgramados);

                if (valorDiaActual == 5) {
                    numeroSemanasGuardadas++;
                }
            }
        }

        return calendario;
    }

}
