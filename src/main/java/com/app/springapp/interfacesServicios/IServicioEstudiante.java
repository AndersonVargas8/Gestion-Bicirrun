package com.app.springapp.interfacesServicios;

import java.util.List;

import com.app.springapp.entity.Estudiante;

public interface IServicioEstudiante {
    public void guardarEstudiante(Estudiante estudiante);
    public void eliminarEstudiante(int id);
    public List<Estudiante> obtenerTodos();
    public Estudiante buscarPorId(int id);
}
