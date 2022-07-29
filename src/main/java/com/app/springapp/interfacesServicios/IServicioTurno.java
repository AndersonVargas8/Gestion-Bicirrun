package com.app.springapp.interfacesServicios;

import java.util.List;

import com.app.springapp.dto.TurnoDTO;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Turno;

public interface IServicioTurno {
    public List<Turno> obtenerTodos();
    public List<Turno> obtenerPorDiaMes(int dia, int mes);
    public List<Turno> obtenerPorEstudiante(Estudiante estudiante);
    public Turno obtenerPorId(long id);
    public TurnoDTO guardarTurno(TurnoDTO turnoDTO);
    public void eliminarTurno(int idTurno);
    public TurnoDTO editarTurno(int id, TurnoDTO turnoDTO);
    public TurnoDTO parcharTurno(int id, TurnoDTO turnoDTO);
}
