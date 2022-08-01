package com.app.springapp.interfacesServicios;

import java.time.LocalDate;
import java.util.List;

import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;

public interface IServicioEstacion {

    public List<Estacion> obtenerTodas();
    public Estacion buscarPorId(int id);
    public List<Estacion> obtenerDisponiblesPorFechaYHorario(LocalDate fecha, Horario horario);
}
