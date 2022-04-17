package com.app.springapp.interfacesServicios;

import java.util.List;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Disponibilidad;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;

public interface IServicioDisponibilidad {
    public int guardarDisponibilidad(Disponibilidad disponibilidad);
    public Disponibilidad consultarDisponibilidadMesDia(int mes, int dia, Cupo cupo);
    public Integer cuposDisponiblesEnDia(int dia, int mes);
    public void actualizarCuposDia(int dia, int mes);
    public List<Horario> horariosDisponiblesDiaMes(int dia, int mes);
    public List<Estacion> estacionesDisponiblesDiaMesHorario(int dia, int mes, int idHorario);
    public List<Integer> diasSinDisponibilidadEnHorario(int mes, int idHorario);
}
