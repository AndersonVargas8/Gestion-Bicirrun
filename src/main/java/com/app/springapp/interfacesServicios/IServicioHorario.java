package com.app.springapp.interfacesServicios;

import java.util.List;

import com.app.springapp.entity.Horario;

public interface IServicioHorario {
    public int guardarHorario(Horario horario);
    public List<Horario> obtenerTodos();
    public Horario buscarPorId(int id);
}
