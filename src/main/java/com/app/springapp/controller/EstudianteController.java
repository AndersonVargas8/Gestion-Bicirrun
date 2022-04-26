package com.app.springapp.controller;

import javax.validation.Valid;

import com.app.springapp.entity.Estudiante;
import com.app.springapp.repository.CarreraRepository;
import com.app.springapp.service.EstudianteService;
import com.app.springapp.service.TurnoService;

import org.hibernate.type.LocalDateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Controller
public class EstudianteController {
    @Autowired
    CarreraRepository repCarrera;

    @Autowired
    EstudianteService serEstudiante;

    @Autowired
    TurnoService serTurno;

    @GetMapping("/estudiantes")
    public String inicio(Model model) {
        model.addAttribute("carreras", repCarrera.findAll());
        model.addAttribute("estudiantes", serEstudiante.obtenerTodos());
        model.addAttribute("estudiante",new Estudiante());
        model.addAttribute("listTab","estudiantes");
        return "estudiante";
    }

    @PostMapping("/estudiantes")
    public String createUser(@Valid @ModelAttribute("estudiante") Estudiante estudiante, BindingResult result, ModelMap model) {
        try {
            serEstudiante.guardarEstudiante(estudiante);
            System.out.println("Guardado");
        } catch (Exception e) {
            System.out.println("ERROR AL GUARDAR");
        }
        model.addAttribute("listTab","estudiantes");
        model.addAttribute("estudiante",new Estudiante());
        model.addAttribute("carreras", repCarrera.findAll());
        model.addAttribute("estudiantes", serEstudiante.obtenerTodos());
        return "redirect:/estudiantes";
    }
    @PostMapping( "/eliminarEstudiante")
    public void eliminarEstudiante(@RequestParam int id) {
        serEstudiante.eliminarEstudiante(id); 
    }
    @GetMapping("/editarEstudiante/{id}")
    public String editarEstudiante(Model model,@PathVariable Long id){
        model.addAttribute("estudiante", serEstudiante.buscarPorId(id));
        model.addAttribute("carreras", repCarrera.findAll());
        model.addAttribute("estudiantes", serEstudiante.obtenerTodos());
        model.addAttribute("listTab","estudiantes");
        return "/estudiantes";
    }

    @GetMapping("/estudiantes/horarioEstudiante/{id}")
    public String horarioEstudiante(Model model,@PathVariable Long id){
        Estudiante estudiante = serEstudiante.buscarPorId(id);
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy");
        model.addAttribute("turnos",serTurno.obtenerPorEstudiante(estudiante));
        model.addAttribute("anio",formatter.format(date));
        model.addAttribute("estudiante",estudiante);
        model.addAttribute("listTab","estudiantes");
        return "estudiantes/horarioEstudiante";
    }
}