package com.app.springapp.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.dto.EstudianteDTO;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.repository.CarreraRepository;
import com.app.springapp.service.EstudianteService;
import com.app.springapp.service.TurnoService;
import com.app.springapp.util.Mapper;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {
    @Autowired
    CarreraRepository repCarrera;

    @Autowired
    EstudianteService serEstudiante;

    @Autowired
    TurnoService serTurno;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String inicio(Model model) {
        model.addAttribute("carreras", repCarrera.findAll());
        model.addAttribute("estudiantes", serEstudiante.obtenerTodos());
        return "estudiante";
    }

    @PostMapping
    public ResponseEntity crearEstudiante(@RequestBody EstudianteDTO estudiante) {
        try {
            estudiante = serEstudiante.guardarEstudiante(Mapper.mapToEstudiante(repCarrera, estudiante));
        } catch (CustomeFieldValidationException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Error en el registro", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<EstudianteDTO>(estudiante, HttpStatus.CREATED);
    }

    @PostMapping("/eliminarEstudiante")
    public void eliminarEstudiante(@RequestParam int id) {
        serEstudiante.eliminarEstudiante(id);
    }

    @PostMapping("/{id}")
    public ResponseEntity editarEstudiante(@PathVariable Long id,@RequestBody EstudianteDTO estudianteDto){
        EstudianteDTO estudiante ;
        try{
             estudiante =  serEstudiante.editarEstudiante(id, estudianteDto);
        }catch(CustomeFieldValidationException e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            return new ResponseEntity<String>("Error al editar", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<EstudianteDTO>(estudiante, HttpStatus.CREATED);
    }

    @GetMapping("/horarioEstudiante/{id}")
    public String horarioEstudiante(Model model, @PathVariable Long id) {
        Estudiante estudiante = serEstudiante.buscarPorId(id);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        model.addAttribute("turnos", serTurno.obtenerPorEstudiante(estudiante));
        model.addAttribute("anio", formatter.format(date));
        model.addAttribute("estudiante", estudiante);
        model.addAttribute("listTab", "estudiantes");
        return "estudiantes/horarioEstudiante";
    }

    @PostMapping("/TablaEstudiantes")
    public String tabla(Model model) {
        model.addAttribute("estudiantes", serEstudiante.obtenerTodos());
        return "estudiantes/tablaEstudiantes::TablaFragment";
    }

}