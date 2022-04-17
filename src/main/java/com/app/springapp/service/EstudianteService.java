package com.app.springapp.service;

import java.util.List;

import com.app.springapp.entity.Estudiante;
import com.app.springapp.interfacesServicios.IServicioEstudiante;
import com.app.springapp.repository.EstudianteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService implements IServicioEstudiante {

    @Autowired
    EstudianteRepository repEstudiante;

    @Override
    public void guardarEstudiante(Estudiante estudiante) {
        repEstudiante.save(estudiante);
    }

    @Override
    public void eliminarEstudiante(int id) {
        repEstudiante.deleteById(new Long(id));
    }

    @Override
    public List<Estudiante> obtenerTodos() {
        return (List<Estudiante>) repEstudiante.findAll();
    }

    @Override
    public Estudiante buscarPorId(int id) {
        return repEstudiante.findById(new Long(id)).get();
    }
    
}
