package com.app.springapp.interfacesServicios;

import java.util.List;

import com.app.springapp.entity.Turno;

public interface IServicioTurno {
    public List<Turno> obtenerTodos();
    public List<Turno> obtenerPorDiaMes(int dia, int mes);
    public int guardarTurno(Turno turno);
}
