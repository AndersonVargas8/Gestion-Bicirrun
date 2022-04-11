package com.app.springapp.interfacesServicios;

import java.util.List;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;

public interface IServicioCupo {
    public List<Cupo> obtenerTodos();
    public Cupo buscarPorEstacionYHorario(Estacion estacion, Horario horario);
    public Cupo buscarPorHorario(Horario horario);
    public Cupo buscarPorCupoGrupo(int cupoGrupoId);
    public int buscarIdPorCupoGrupoHorario(int idHorario);
    public Cupo buscarPorId(int id);
    public Integer cantidadCupos();
}
