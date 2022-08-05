package com.app.springapp.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.dto.Calendario;
import com.app.springapp.dto.TurnoDTO;
import com.app.springapp.dto.TurnosEstaciones;
import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Horario;
import com.app.springapp.entity.Turno;
import com.app.springapp.interfacesServicios.IServicioCupo;
import com.app.springapp.interfacesServicios.IServicioEstacion;
import com.app.springapp.interfacesServicios.IServicioEstudiante;
import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.interfacesServicios.IServicioTurno;
import com.app.springapp.model.Mes;
import com.app.springapp.model.TurnosCompletos;
import com.app.springapp.repository.EstadoTurnoRepository;
import com.app.springapp.repository.TurnoRepository;
import com.app.springapp.util.Mapper;

@Service
public class TurnoService implements IServicioTurno {
    @Autowired
    TurnoRepository repTurno;

    @Autowired
    IServicioCupo serCupo;

    @Lazy
    @Autowired
    IServicioEstacion serEstacion;

    @Autowired
    IServicioEstudiante serEstudiante;

    @Lazy
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

    @Override
    public List<Turno> obtenerPorFechaYEstacion(LocalDate fecha, Estacion estacion) {
        int dia = fecha.getDayOfMonth(), mes = fecha.getMonthValue(), anio = fecha.getYear();
        List<Turno> turnos = repTurno.findByDiaAndMesAndAnioAndEstacion(dia, mes, anio, estacion);
        return turnos;
    }

    @Override
    public List<Turno> obtenerPorFechaYEstacionYHorario(LocalDate fecha, Estacion estacion, Horario horario) {
        int dia = fecha.getDayOfMonth(), mes = fecha.getMonthValue(), anio = fecha.getYear();
        List<Turno> turnos = repTurno.findByDiaAndMesAndAnioAndEstacionAndHorario(dia, mes, anio, estacion, horario);
        return turnos;
    }

    @Override
    public int sumaTurnosPorFechaYHorario(LocalDate fecha, Horario horario) {
        int dia = fecha.getDayOfMonth(), mes = fecha.getMonthValue(), anio = fecha.getYear();
        int numeroTurnos = (int) repTurno.countByDiaAndMesAndAnioAndHorario(dia, mes, anio, horario);
        return numeroTurnos;
    }

    @Override
    public int sumaTurnosPorFechaYHorarioYEstacion(LocalDate fecha, Horario horario, Estacion estacion) {
        int dia = fecha.getDayOfMonth(), mes = fecha.getMonthValue(), anio = fecha.getYear();
        int numeroTurnos = (int) repTurno.countByDiaAndMesAndAnioAndEstacionAndHorario(dia, mes, anio, estacion,
                horario);
        return numeroTurnos;
    }

    @Override
    public boolean hayTurnosDisponibles(LocalDate fecha, Horario horario) {
        int dia = fecha.getDayOfMonth(), mes = fecha.getMonthValue(), anio = fecha.getYear();
        TurnosCompletos turnosCom = repTurno.getTurnosCompletos(anio);

        return !turnosCom.estaCompletoHorario(mes, dia, (int) horario.getId());
    }

    /**
     * Retorna la cantidad de turnos programados en la misma fecha, estación y
     * horario del turno proporcionado
     * Tiene en cuenta si el cupo es compartido
     * 
     * @param turno
     * @return Número de turnos programados
     * @throws CustomeFieldValidationException
     */
    public long cantidadTurnosProgramados(Turno turno) throws CustomeFieldValidationException {
        Horario horario = turno.getHorario();

        Cupo cupo = null;
        try {
            cupo = serCupo.buscarPorEstacionYHorario(turno.getEstacion(), turno.getHorario());
        } catch (Exception e) {
            throw new CustomeFieldValidationException("El horario no está disponible para la estación proporcionada");
        }
        if (cupo == null)
            throw new CustomeFieldValidationException("El horario no está disponible para la estación proporcionada");

        long numeroTurnos = repTurno.countByDiaAndMesAndAnioAndEstacionAndHorario(turno.getDia(), turno.getMes(),
                turno.getAnio(), turno.getEstacion(), horario);

        if (cupo.tieneCupoCompartido()) {
            horario = cupo.getCupoCompartido().getHorario();
            numeroTurnos += repTurno.countByDiaAndMesAndAnioAndEstacionAndHorario(turno.getDia(), turno.getMes(),
                    turno.getAnio(), turno.getEstacion(), horario);
        }

        return numeroTurnos;
    }

    /**
     * Retorna la cantidad de turnos programados en la fecha, estación y
     * horario proporcionados
     * Tiene en cuenta si el cupo es compartido
     * 
     * @param turno
     * @return Número de turnos programados
     * @throws CustomeFieldValidationException
     */
    public long cantidadTurnosProgramados(int dia, int mes, int anio, Estacion estacion, Horario horario)
            throws CustomeFieldValidationException {
        Turno turno = new Turno();
        turno.setDia(dia);
        turno.setMes(mes);
        turno.setAnio(anio);
        turno.setEstacion(estacion);
        turno.setHorario(horario);
        return cantidadTurnosProgramados(turno);
    }

    /**
     * Verifica si existe disponibilidad para crear un turno con la fecha, estación
     * y horario que contiene.
     * 
     * @param turno
     * @return True si está disponible. False en otro caso
     * @throws CustomeFieldValidationException
     */
    public boolean turnoEstaDisponible(Turno turno) throws CustomeFieldValidationException {
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
            if (cupo.tieneCupoCompartido()) {
                horario = cupo.getCupoCompartido().getHorario();
                numeroTurnos += repTurno.countByDiaAndMesAndAnioAndHorarioAndEstudiante(turno.getDia(), turno.getMes(),
                        turno.getAnio(), horario, turno.getEstudiante());
                break;
            }
        }

        if (numeroTurnos > 0)
            return false;

        return true;
    }

    /**
     * Calcula la cantidad de turnos programados en la fecha y horario
     * proporcionados
     * 
     * @param horario
     * @return
     */
    public int turnosProgramadosPorHorario(int dia, int mes, int anio, Horario horario) {
        long numeroTurnosProgramados = repTurno.countByDiaAndMesAndAnioAndHorario(dia, mes, anio, horario);

        return (int) numeroTurnosProgramados;
    }

    private void actualizarArchivosAgregarTurno(Turno turno) throws CustomeFieldValidationException {
        int dia = turno.getDia(), mes = turno.getMes(), anio = turno.getAnio();
        Horario horarioPrincipal = turno.getHorario();
        // Día completo por horario
        List<Cupo> cupos = serCupo.buscarPorHorario(turno.getHorario());
        // Revisa si tiene un cupo independiente
        for (Cupo cupo : cupos) {
            if (!cupo.getCupos_independientes().isEmpty()) {
                horarioPrincipal = cupo.getCupoCompartido().getHorario();
                cupos = serCupo.buscarPorHorario(cupo.getCupoCompartido().getHorario());
                break;
            }
        }
        Set<Horario> horariosCupoCompartido = new HashSet<>();
        int numeroCupos = cupos.size(), sumaCuposCompletos = 0;
        int sumaCuposCompartidos = 0, sumaCuposCompartidosCompletos = 0;

        for (Cupo cupo : cupos) {
            long cantTurnosProg = cantidadTurnosProgramados(dia, mes, anio, cupo.getEstacion(), cupo.getHorario());
            long cantCupos = cupo.getNum_cupos();
            if (cupo.tieneCupoCompartido()) {
                cantCupos += cupo.getCupoCompartido().getNum_cupos();
                sumaCuposCompartidos++;
                if (cantTurnosProg >= cantCupos) {
                    sumaCuposCompartidosCompletos++;
                    horariosCupoCompartido.add(cupo.getCupoCompartido().getHorario());
                }
            }
            if (cantTurnosProg >= cantCupos) {
                sumaCuposCompletos++;
            }
        }

        if (numeroCupos == sumaCuposCompletos) {
            repTurno.agregarDiaTurnosCompletosHorario(turno.getDia(), turno.getMes(), turno.getAnio(),
                    (int) horarioPrincipal.getId());
        }

        if (sumaCuposCompartidos == sumaCuposCompartidosCompletos) {
            for (Horario horario : horariosCupoCompartido) {
                repTurno.agregarDiaTurnosCompletosHorario(turno.getDia(), turno.getMes(), turno.getAnio(),
                        (int) horario.getId());
            }
        }

        // Día completo totalmente
        int turnosProgramados = repTurno.agregarTurnoProgramado(turno.getDia(), turno.getMes(), turno.getAnio());
        int cantidadCupos = serCupo.cantidadCuposPorFecha(turno.getDia(), turno.getMes(), turno.getAnio());
        if (turnosProgramados >= cantidadCupos) {
            repTurno.agregarDiaTurnosCompletos(turno.getDia(), turno.getMes(), turno.getAnio());
        }
    }

    private void actualizarArchivosEliminarTurno(Turno turno) {
        int dia = turno.getDia(), mes = turno.getMes(), anio = turno.getAnio();
        repTurno.eliminarTurnoProgramado(dia, mes, anio);

        // Dia completo horario
        repTurno.eliminarDiaTurnosCompletos(dia, mes, anio, (int) turno.getHorario().getId());

        Cupo cupo = serCupo.buscarPorEstacionYHorario(turno.getEstacion(), turno.getHorario());

        if (cupo.tieneCupoCompartido()) {
            repTurno.eliminarDiaTurnosCompletos(dia, mes, anio, (int) cupo.getCupoCompartido().getHorario().getId());
        }

    }

    @Override
    public TurnoDTO guardarTurno(TurnoDTO turnoDTO) throws CustomeFieldValidationException {
        Turno turno = Mapper.mapToTurno(turnoDTO, serEstacion, serEstudiante, serHorario, repEstadoTurno);

        // Validar dia disponible
        if (!turnoEstaDisponible(turno)) {
            throw new CustomeFieldValidationException(
                    "No hay turnos disponibles en la fecha, horario y estación indicados");
        }

        // Validar estudiante disponibnle
        if (!estudianteEstaDisponible(turno)) {
            throw new CustomeFieldValidationException("El estudiante ya tiene un turno programado en el mismo horario");
        }

        repTurno.save(turno);

        actualizarArchivosAgregarTurno(turno);

        turnoDTO = Mapper.mapToTurnoDTO(turno);
        return turnoDTO;
    }

    @Override
    public void eliminarTurno(int idTurno) throws CustomeFieldValidationException {
        if (!repTurno.existsById(new Long(idTurno))) {
            throw new CustomeFieldValidationException("No existe un turno con el id proporcionado");
        }
        Turno turno = obtenerPorId(idTurno);
        repTurno.deleteById(new Long(idTurno));
        actualizarArchivosEliminarTurno(turno);
    }

    @Override
    public TurnoDTO editarTurno(int idTurno, TurnoDTO turnoDTO) throws CustomeFieldValidationException {
        Turno turnoActual = new Turno();
        boolean crearNuevo = true;
        if (repTurno.existsById(new Long(idTurno))) {
            turnoActual = obtenerPorId(idTurno).clone();
            crearNuevo = false;
        }

        Turno turno = Mapper.mapToTurno(turnoDTO, serEstacion, serEstudiante, serHorario, repEstadoTurno);
        turno.setId(idTurno);

        /*
         * Si hay que crear turno, o si la fecha cambia, o si (la estación y el horario
         * cambian) o si horario cambia
         */
        if (crearNuevo || turnoActual.compareTo(turno, true) != 0
                || (!turnoActual.getEstacion().equals(turno.getEstacion())
                        && !turnoActual.getHorario().equals(turno.getHorario()))
                || !turnoActual.getHorario().equals(turno.getHorario())) {

            // Validar dia disponible
            if (!turnoEstaDisponible(turno)) {
                throw new CustomeFieldValidationException(
                        "No hay turnos disponibles en la fecha, horario y estación indicados");
            }
            // Validar estudiante disponibnle
            if (!estudianteEstaDisponible(turno)) {
                throw new CustomeFieldValidationException(
                        "El estudiante ya tiene un turno programado en el mismo horario");
            }

        } else if ((turnoActual.getEstacion().equals(turno.getEstacion())
                && !turnoActual.getHorario().equals(turno.getHorario()))
                || !turnoActual.getEstacion().equals(turno.getEstacion())) {
            // Validar dia disponible
            if (!turnoEstaDisponible(turno)) {
                throw new CustomeFieldValidationException(
                        "No hay turnos disponibles en la fecha, horario y estación indicados");
            }
        }

        if (crearNuevo || !turnoActual.getEstudiante().equals(turno.getEstudiante())) {
            // Validar estudiante disponibnle
            if (!estudianteEstaDisponible(turno)) {
                throw new CustomeFieldValidationException(
                        "El estudiante ya tiene un turno programado en el mismo horario");
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
    public TurnoDTO parcharTurno(int idTurno, TurnoDTO turnoDTO) throws CustomeFieldValidationException {
        if (!repTurno.existsById(new Long(idTurno))) {
            throw new CustomeFieldValidationException("No existe un turno con el id proporcionado");
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
                throw new CustomeFieldValidationException(
                        "No hay turnos disponibles en la fecha, horario y estación indicados");
            }
        }

        if (!turnoActual.getEstudiante().equals(turno.getEstudiante())) {
            // Validar estudiante disponibnle
            if (!estudianteEstaDisponible(turno)) {
                throw new CustomeFieldValidationException(
                        "El estudiante ya tiene un turno programado en el mismo horario");
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

    @Override
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

    @Override
    public DiasDeshabilitados obtenerDiasDeshabilitados() {
        DiasDeshabilitados diasD = new DiasDeshabilitados();
        diasD.diasDeshabilitados = new ArrayList<>();

        List<String> fechas = repTurno.getFechasCompletas();

        diasD.fechasDeshabilitadas = fechas;

        return diasD;

    }

    @Override
    public DiasDeshabilitados obtenerDiasDeshabilitados(int idHorario) {
        DiasDeshabilitados diasD = new DiasDeshabilitados();
        diasD.diasDeshabilitados = new ArrayList<>();

        Horario horario = serHorario.buscarPorId(idHorario);
        Set<String> diasNoDisp = horario.getDiasNoDisponibles();

        for (String dia : diasNoDisp) {
            diasD.diasDeshabilitados.add(Calendario.convertirNombreDiaANumeroSemana(dia));
        }

        List<String> fechas = repTurno.getFechasCompletas(idHorario);

        diasD.fechasDeshabilitadas = fechas;

        return diasD;
    }

    @Override
    public TurnosEstaciones obtenerTurnosEstaciones(LocalDate fecha) {
        int dia = fecha.getDayOfMonth(), mes = fecha.getMonthValue(), anio = fecha.getYear();
        String fechaFormat = dia + "-" + mes + "-" + anio;
        TurnosEstaciones turnosEstaciones = new TurnosEstaciones(fechaFormat);
        List<Estacion> estaciones = serEstacion.obtenerTodas();
        List<Horario> horarios = serHorario.obtenerTodos();
        for (Estacion estacion : estaciones) {
            for (Horario horario : horarios) {
                List<Turno> turnos = obtenerPorFechaYEstacionYHorario(fecha, estacion, horario);
                for (Turno turno : turnos) {
                    TurnoDTO turnoDTO = Mapper.mapToTurnoDTOFull(turno);
                    turnosEstaciones.agregarTurno(turnoDTO);
                }
                int numTurnos = 0;
                try {
                    numTurnos = (int) cantidadTurnosProgramados(dia, mes, anio, estacion, horario);
                } catch (CustomeFieldValidationException e) {
                }

                int numCupos = serCupo.cantidadCupos(fecha, horario, estacion);

                if (numTurnos < numCupos) {
                    turnosEstaciones.agregarCuposDisponibles(estacion, horario, numCupos - numTurnos);
                }
            }
        }

        return turnosEstaciones;
    }

}
