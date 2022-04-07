package com.app.springapp.interfacesServicios;

import java.util.Optional;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Disponibilidad;

public interface IServicioDisponibilidad {
    public int guardarDisponibilidad(Disponibilidad disponibilidad);
    public Disponibilidad consultarDisponibilidadMesDia(int mes, int dia, Cupo cupo);
    public Integer cuposDisponiblesEnDia(int dia, int mes);
    public void actualizarCuposDia(int dia, int mes);
}
