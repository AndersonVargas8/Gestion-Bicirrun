package com.app.springapp.interfacesServicios;

import java.util.List;

import com.app.springapp.entity.Estacion;

public interface IServicioEstacion {

    public List<Estacion> obtenerTodas();
    public Estacion buscarPorId(int id);
}
