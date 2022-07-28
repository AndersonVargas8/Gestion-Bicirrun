package com.app.springapp.interfacesServicios;

import java.util.List;

import com.app.springapp.dto.TurnoDTO;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Turno;

public interface IServicioTurno {
    public List<Turno> obtenerTodos();
    public List<Turno> obtenerPorDiaMes(int dia, int mes);
    public Turno guardarTurno(TurnoDTO turnoDTO);
    public List<Turno> obtenerPorEstudiante(Estudiante estudiante);
    public void eliminarTurno(Turno turno);
    public Turno obtenerPorId(long id);
}
