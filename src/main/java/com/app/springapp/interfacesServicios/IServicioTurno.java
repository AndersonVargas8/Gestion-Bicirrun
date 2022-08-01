package com.app.springapp.interfacesServicios;

import java.time.LocalDate;
import java.util.List;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.dto.Calendario;
import com.app.springapp.dto.TurnoDTO;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Horario;
import com.app.springapp.entity.Turno;

public interface IServicioTurno {
    public List<Turno> obtenerTodos();

    public List<Turno> obtenerPorDiaMes(int dia, int mes);

    public List<Turno> obtenerPorEstudiante(Estudiante estudiante);

    public Turno obtenerPorId(long id);

    public TurnoDTO guardarTurno(TurnoDTO turnoDTO) throws CustomeFieldValidationException ;

    public void eliminarTurno(int idTurno) throws CustomeFieldValidationException ;

    public int sumaTurnosPorFechaYHorario(LocalDate fecha, Horario horario);
    
    public int sumaTurnosPorFechaYHorarioYEstacion(LocalDate fecha, Horario horario, Estacion estacion);

    public boolean hayTurnosDisponibles(LocalDate fecha, Horario horario);

    public TurnoDTO editarTurno(int id, TurnoDTO turnoDTO) throws CustomeFieldValidationException ;

    public TurnoDTO parcharTurno(int id, TurnoDTO turnoDTO) throws CustomeFieldValidationException ;

    /**
     * Retornna un objeto calendario con la información de turnos programados y cupos totales en el mes proporcionado
     * @param mes
     * @param anio
     * @return
     */
    public Calendario getCalendario(int mes, int anio);

    public class DiasDeshabilitados{
        public List<Integer> diasDeshabilitados;
        public List<String> fechasDeshabilitadas;
    }
    public DiasDeshabilitados obtenerDiasDeshabilitados();
    public DiasDeshabilitados obtenerDiasDeshabilitados(int idHorario);
}
