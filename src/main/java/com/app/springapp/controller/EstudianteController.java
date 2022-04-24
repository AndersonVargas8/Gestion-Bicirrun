package com.app.springapp.controller;

import javax.validation.Valid;

import com.app.springapp.entity.Estudiante;
import com.app.springapp.repository.CarreraRepository;
import com.app.springapp.service.EstudianteService;

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

@Controller
public class EstudianteController {
    @Autowired
    CarreraRepository repCarrera;

    @Autowired
    EstudianteService serEstudiante;

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
        return "estudiante";
    }
    @PostMapping( "/eliminarEstudiante")
    public void eliminarEstudiante(@RequestParam int id) {
        serEstudiante.eliminarEstudiante(id); 
    }
    @GetMapping("/editarEstudiante/{id}")
    public String editarEstudiante(Model model,@PathVariable int id){
        model.addAttribute("estudiante", serEstudiante.buscarPorId(id));
        model.addAttribute("carreras", repCarrera.findAll());
        model.addAttribute("estudiantes", serEstudiante.obtenerTodos());
        model.addAttribute("listTab","estudiantes");
        return "/estudiantes";
    }
}