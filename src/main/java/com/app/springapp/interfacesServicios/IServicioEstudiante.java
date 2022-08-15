package com.app.springapp.interfacesServicios;

import java.util.List;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.dto.EstudianteDTO;
import com.app.springapp.entity.Estudiante;

public interface IServicioEstudiante {
    public  EstudianteDTO guardarEstudiante(Estudiante estudiante) throws CustomeFieldValidationException ;
    public void eliminarEstudiante(int id);
    public List<Estudiante> obtenerTodos();
    public Estudiante buscarPorId(Long id);
    public Estudiante buscarPorDocumento(String documento);
}
