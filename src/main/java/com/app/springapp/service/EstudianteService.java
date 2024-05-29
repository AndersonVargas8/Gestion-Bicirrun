package com.app.springapp.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(EstudianteService.class.getName());

    @Autowired
    EstudianteRepository repEstudiante;

    @Autowired
    CarreraRepository repCarrera;

    @Override
    public EstudianteDTO guardarEstudiante(Estudiante estudiante) throws CustomeFieldValidationException {
        logger.info("Guardando estudiante: " + estudiante);

        if (estudiante.getDocumento() == null || estudiante.getDocumento().isEmpty()) {
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
        EstudianteDTO result = Mapper.mapToEstudianteDTO(repEstudiante.save(estudiante));
        logger.info("Estudiante guardado exitosamente: " + estudiante);
        return result;
    }

    @Override
    public EstudianteDTO editarEstudiante(Long idEstudiante, EstudianteDTO estudianteDTO)
            throws CustomeFieldValidationException {
        logger.info("Editando estudiante con ID: " + idEstudiante);

        if (estudianteDTO.carrera == 0) {
            throw new CustomeFieldValidationException("No se especificó la carrera");
        }
        Estudiante estudiante = Mapper.mapToEstudiante(repCarrera, estudianteDTO);
        estudiante.setId(idEstudiante);

        if (estudiante.getDocumento() == null || estudiante.getDocumento().isEmpty()) {
            throw new CustomeFieldValidationException("Documento vacío");
        }
        if (estudiante.getApellidos() == null || estudiante.getApellidos().isEmpty()) {
            throw new CustomeFieldValidationException("Apellido vacío");
        }
        if (estudiante.getNombres() == null || estudiante.getNombres().isEmpty()) {
            throw new CustomeFieldValidationException("Nombre vacío");
        }
        EstudianteDTO result = Mapper.mapToEstudianteDTO(repEstudiante.save(estudiante));
        logger.info("Estudiante editado exitosamente: " + estudiante);
        return result;
    }

    @Override
    public void eliminarEstudiante(int id) throws CustomeFieldValidationException {
        logger.info("Eliminando estudiante con ID: " + id);
        Estudiante estudiante = buscarPorId(new Long(id));
        if (estudiante == null) {
            throw new CustomeFieldValidationException("El estudiante a eliminar no está registrado");
        }
        repEstudiante.deleteById(new Long(id));
        logger.info("Estudiante eliminado exitosamente con ID: " + id);
    }

    @Override
    public List<Estudiante> obtenerTodos() {
        logger.info("Obteniendo todos los estudiantes");
        return (List<Estudiante>) repEstudiante.findAll();
    }

    @Override
    public Estudiante buscarPorId(Long id) {
        logger.info("Buscando estudiante por ID: " + id);
        Estudiante estudiante = null;
        Optional<Estudiante> opEstudiante = repEstudiante.findById(new Long(id));

        if (opEstudiante.isPresent()) {
            estudiante = opEstudiante.get();
        }
        logger.info("Estudiante encontrado: " + estudiante);
        return estudiante;
    }

    @Override
    public Estudiante buscarPorDocumento(String documento) {
        logger.info("Buscando estudiante por documento: " + documento);
        Estudiante estudiante = null;
        Optional<Estudiante> opEstudiante = repEstudiante.findByDocumento(documento);

        if (opEstudiante.isPresent()) {
            estudiante = opEstudiante.get();
        }
        logger.info("Estudiante encontrado: " + estudiante);
        return estudiante;
    }

    public List<EstudianteDTO> obtenerIdYNombre() {
        logger.info("Obteniendo IDs y nombres de todos los estudiantes");
        List<EstudianteDTO> resultado = repEstudiante.findAllIdAndNombres();
        logger.info("IDs y nombres obtenidos exitosamente");
        return resultado;
    }
}
