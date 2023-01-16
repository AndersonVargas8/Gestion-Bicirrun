package com.app.springapp.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.dto.Calendario;
import com.app.springapp.dto.CuposYTurnosEstacionesHorario;
import com.app.springapp.dto.TurnoDTO;
import com.app.springapp.dto.TurnosEstaciones;
import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Horario;
import com.app.springapp.entity.Horario.Dia;
import com.app.springapp.entity.Turno;
import com.app.springapp.interfacesServicios.IServicioEstacion;
import com.app.springapp.interfacesServicios.IServicioEstudiante;
import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.interfacesServicios.IServicioTurno;
import com.app.springapp.repository.CupoRepository;
import com.app.springapp.repository.EstadoTurnoRepository;
import com.app.springapp.repository.HorarioRepository;
import com.app.springapp.repository.HorarioRepository.IHorariosDiasNoDisponibles;
import com.app.springapp.repository.TurnoRepository;
import com.app.springapp.util.Mapper;

@Service
public class TurnoService implements IServicioTurno {
    @Autowired
    TurnoRepository repTurno;

    @Autowired
    CupoService serCupo;

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

    @Autowired
    CupoRepository repCupo;

    @Autowired
    HorarioRepository repHorario;

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
    public int sumaTurnosFecha(LocalDate fecha) {
        int dia = fecha.getDayOfMonth(), mes = fecha.getMonthValue(), anio = fecha.getYear();
        int numeroTurnos = (int) repTurno.countByDiaAndMesAndAnio(dia, mes, anio);
        return numeroTurnos;
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
        int valorDiaActual = DayOfWeek.from(fecha).getValue();
        String nombreDia = Calendario.convertirNumeroADia(valorDiaActual);
        if (horario.diaNoDisponible(nombreDia)) {
            return false;
        }
        int numeroCupos = serCupo.cantidadCuposPorHorario(horario);
        int numeroTurnos = sumaTurnosPorFechaYHorario(fecha, horario);

        List<Cupo> cupos = serCupo.buscarPorHorario(horario);
        Set<Horario> horariosDependientes = new HashSet<>();
        for (Cupo cupo : cupos) {
            if (cupo.tieneCupoCompartido()) {
                if (!cupo.getCupos_dependientes().isEmpty()) {
                    horariosDependientes.add(cupo.getCupoCompartido().getHorario());
                } else {
                    numeroTurnos += sumaTurnosPorFechaYHorarioYEstacion(fecha, cupo.getCupoCompartido().getHorario(),
                            cupo.getEstacion());
                }

                numeroCupos += cupo.getCupoCompartido().getNum_cupos();
            }
        }

        for (Horario horarioIt : horariosDependientes) {
            numeroTurnos += sumaTurnosPorFechaYHorario(fecha, horarioIt);
        }

        return numeroTurnos < numeroCupos;
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

        // HashMap con los cupos por día
        HashMap<Integer, Integer> cuposPorDia = serCupo.cantidadCuposAlDia();

        // Map con los turnos programados en cada dia
        Map<Integer, Integer> turnosPorDia = repTurno.sumAllTurnosByMesAndAnio(mes, anio);
        int numeroSemanasGuardadas = 0;

        for (int diaMes = 1; diaMes <= diasDelMes; diaMes++) {
            LocalDate dia = fecha.withDayOfMonth(diaMes);
            int valorDiaActual = DayOfWeek.from(dia).getValue();

            if (valorDiaActual <= 5) {// Si el día no es sábado ni domingo
                int cuposDia = cuposPorDia.get(valorDiaActual);
                int turnosProgramados = 0;

                if (turnosPorDia.containsKey(diaMes)) {
                    turnosProgramados = turnosPorDia.get(diaMes);
                }

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
        LocalDate fecha = LocalDate.now();

        // Lista de días con algún cupo programado después de hoy(incluido)
        List<Object[]> respuesta = repTurno.findDistinctPostFecha(fecha.getDayOfMonth(), fecha.getMonthValue(),
                fecha.getYear());

        // HashMap con los cupos por día
        HashMap<Integer, Integer> cuposPorDia = serCupo.cantidadCuposAlDia();

        List<String> fechas = new ArrayList<>();

        for (Object[] obj : respuesta) {
            fecha = LocalDate.of((int) obj[2], (int) obj[1], (int) obj[0]);
            int valorDiaActual = DayOfWeek.from(fecha).getValue();
            int cuposDia = cuposPorDia.get(valorDiaActual);
            int turnosProgramados = sumaTurnosFecha(fecha);

            if (turnosProgramados >= cuposDia)
                fechas.add(String.valueOf(obj[0]) + "-" + String.valueOf(obj[1]) + "-" + String.valueOf(obj[2]));
        }

        diasD.fechasDeshabilitadas = fechas;

        return diasD;

    }

    @Override
    public DiasDeshabilitados obtenerDiasDeshabilitados(int idHorario) {
        DiasDeshabilitados diasD = new DiasDeshabilitados();
        diasD.diasDeshabilitados = new ArrayList<>();

        Horario horario = serHorario.buscarPorId(idHorario);
        Set<Dia> diasNoDisp = horario.getDiasNoDisponibles();

        for (Dia dia : diasNoDisp) {
            diasD.diasDeshabilitados.add(Calendario.convertirNombreDiaANumeroSemana(dia.name()));
        }

        LocalDate fecha = LocalDate.now();
        // Lista de días con algún cupo programado después de hoy(incluido)
        List<Object[]> respuesta = repTurno.findDistinctByHorarioPostFecha(fecha.getDayOfMonth(), fecha.getMonthValue(),
                fecha.getYear(), idHorario);

        List<String> fechas = new ArrayList<>();

        for (Object[] obj : respuesta) {
            fecha = LocalDate.of((int) obj[2], (int) obj[1], (int) obj[0]);
            int cuposDia = serCupo.cantidadCuposPorHorario(horario);

            int turnosProgramados = sumaTurnosPorFechaYHorario(fecha, horario);

            List<Cupo> cupos = serCupo.buscarPorHorario(horario);
            Set<Horario> horariosDependientes = new HashSet<>();
            for (Cupo cupo : cupos) {
                if (cupo.tieneCupoCompartido()) {
                    if (!cupo.getCupos_dependientes().isEmpty()) {
                        horariosDependientes.add(cupo.getCupoCompartido().getHorario());
                    } else {
                        turnosProgramados += sumaTurnosPorFechaYHorarioYEstacion(fecha,
                                cupo.getCupoCompartido().getHorario(), cupo.getEstacion());
                    }

                    cuposDia += cupo.getCupoCompartido().getNum_cupos();
                }
            }

            for (Horario horarioIt : horariosDependientes) {
                turnosProgramados += sumaTurnosPorFechaYHorario(fecha, horarioIt);
            }

            if (turnosProgramados >= cuposDia)
                fechas.add(String.valueOf(obj[0]) + "-" + String.valueOf(obj[1]) + "-" + String.valueOf(obj[2]));
        }

        diasD.fechasDeshabilitadas = fechas;

        return diasD;
    }

    @Override
    public TurnosEstaciones obtenerTurnosEstaciones(LocalDate fecha) {
        int dia = fecha.getDayOfMonth(), mes = fecha.getMonthValue(), anio = fecha.getYear();
        String fechaFormat = dia + "-" + mes + "-" + anio;
        TurnosEstaciones turnosEstaciones = new TurnosEstaciones(fechaFormat);
        CuposYTurnosEstacionesHorario cupos = new CuposYTurnosEstacionesHorario();

        // Se obtienen todos los horarios programados en la fecha
        List<TurnoDTO> turnos = repTurno.findByFecha(dia, mes, anio);
        // Se obtienen todos los cupos en cada estación en cada horario
        cupos = repCupo.sumCuposGroupByEstacionHorario(cupos);
        // Se obtienen los horarios dependientes
        Map<Long, Long> horariosDependientes = repHorario.findHorariosDependientes();
        // Se obtienen los horarios no disponibles
        List<IHorariosDiasNoDisponibles> horariosNoDisponiblesList = repHorario.findHorariosDiasNoDisponibles();
        List<Long> horariosNoDisponibles = new ArrayList<>();
        // Se obtienen todas las estaciones
        List<Estacion> estaciones = serEstacion.obtenerTodas();
        // Se obtienen todos los horarios
        List<Horario> horarios = serHorario.obtenerTodos();
        
        int valorDiaActual = DayOfWeek.from(fecha).getValue();
        String nombreDia = Calendario.convertirNumeroADia(valorDiaActual);
        
        for(IHorariosDiasNoDisponibles horarioNoDisp : horariosNoDisponiblesList){
            if(horarioNoDisp.getDia().equals(nombreDia)){
                horariosNoDisponibles.add(horarioNoDisp.getHorarioId());
            }
        }

        for (TurnoDTO turno : turnos) {
            turnosEstaciones.agregarTurno(turno);
            long idHorario = turno.idHorario;

            if (horariosDependientes.containsKey(idHorario))
                idHorario = horariosDependientes.get(idHorario);

            cupos.substractCupo(turno.idEstacion, idHorario);
        }
        
        for(Estacion estacion: estaciones){
            for(Horario horario: horarios){
                long idHorario = horario.getId();
                if(horariosNoDisponibles.contains(idHorario))
                    continue;
            
                if (horariosDependientes.containsKey(idHorario))
                    idHorario = horariosDependientes.get(idHorario);
                
                int numCupos = cupos.getCupos(estacion.getId(), idHorario);

                if(numCupos > 0){
                    turnosEstaciones.agregarCuposDisponibles(estacion, horario, numCupos);
                }
            }
        }

        return turnosEstaciones;
    }

}
