package com.app.springapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.dto.EstudianteDTO;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.interfacesServicios.IServicioEstudiante;
import com.app.springapp.repository.CarreraRepository;
import com.app.springapp.repository.EstudianteRepository;
import com.app.springapp.util.Mapper;

@Service
public class EstudianteService implements IServicioEstudiante {

    @Autowired
    EstudianteRepository repEstudiante;

    @Autowired
    CarreraRepository repCarrera;

    @Override
    public EstudianteDTO guardarEstudiante(Estudiante estudiante) throws CustomeFieldValidationException {
        if (estudiante.getDocumento().equals(null) || estudiante.getDocumento().isEmpty()) {
            throw new CustomeFieldValidationException("Documento vacío");
        }
        if (buscarPorDocumento(estudiante.getDocumento()) != null) {
            throw new CustomeFieldValidationException("El estudiante ya existe");
        }
        if (estudiante.getApellidos() == null || estudiante.getApellidos().isEmpty()) {
            throw new CustomeFieldValidationException("Apellido vacío");
        }
        if (estudiante.getNombres() == null || estudiante.getNombres().isEmpty()) {
            throw new CustomeFieldValidationException("Nombre vacío");
        }
        return Mapper.mapToEstudianteDTO(repEstudiante.save(estudiante));
    }

    @Override
    public EstudianteDTO editarEstudiante(Long idEstudiante, EstudianteDTO estudianteDTO)
            throws CustomeFieldValidationException {
        
        if (estudianteDTO.carrera==0) {
            throw new CustomeFieldValidationException("No se especificó la carrera");
        }
        Estudiante estudiante = Mapper.mapToEstudiante(repCarrera, estudianteDTO);
        estudiante.setId(idEstudiante);

        if (estudiante.getDocumento().equals(null) ||
                estudiante.getDocumento().isEmpty()) {
            throw new CustomeFieldValidationException("Documento vacío");
        }

        if (estudiante.getApellidos() == null || estudiante.getApellidos().isEmpty()) {
            throw new CustomeFieldValidationException("Apellido vacío");
        }
        if (estudiante.getNombres() == null || estudiante.getNombres().isEmpty()) {
            throw new CustomeFieldValidationException("Nombre vacío");
        }

        return Mapper.mapToEstudianteDTO(repEstudiante.save(estudiante));
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
    public Estudiante buscarPorId(Long id) {
        Estudiante estudiante = null;
        Optional<Estudiante> opEstudiante = repEstudiante.findById(new Long(id));

        if (opEstudiante.isPresent()) {
            estudiante = opEstudiante.get();
        }
        return estudiante;
    }

    @Override
    public Estudiante buscarPorDocumento(String documento) {
        Estudiante estudiante = null;
        Optional<Estudiante> opEstudiante = repEstudiante.findByDocumento(documento);

        if (opEstudiante.isPresent()) {
            estudiante = opEstudiante.get();
        }
        return estudiante;
    }

}
