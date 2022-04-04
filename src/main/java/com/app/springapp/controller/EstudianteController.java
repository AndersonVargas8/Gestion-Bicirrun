package com.app.springapp.controller;

import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Turno;
import com.app.springapp.repository.CarreraRepository;
import com.app.springapp.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class EstudianteController {
    @Autowired
    CarreraRepository repCarrera;
    @Autowired
    EstudianteRepository repEstudiante;

    @GetMapping("/estudiantes")
    public String inicio(Model model) {
        model.addAttribute("carreras", repCarrera.findAll());
        model.addAttribute("estudiantes", repEstudiante.findAll());
        model.addAttribute("estudiante",new Estudiante());

        return "estudiante";
    }

    @PostMapping("/estudiantes")
    public String createUser(@Valid @ModelAttribute("estudiante") Estudiante estudiante, BindingResult result, ModelMap model) {
        try {
            repEstudiante.save(estudiante);
            System.out.println("Guardado");
        } catch (Exception e) {
            System.out.println("ERROR AL GUARDAR");
        }
        model.addAttribute("carreras", repCarrera.findAll());
        model.addAttribute("estudiantes", repEstudiante.findAll());
        return "estudiante";
    }
    @GetMapping( "/eliminar/{id}")
    public String eliminarProducto(@ModelAttribute Estudiante estudiante, RedirectAttributes redirectAttrs, BindingResult result, ModelMap model,@PathVariable int id) {
        redirectAttrs
                .addFlashAttribute("mensaje", "Eliminado correctamente")
                .addFlashAttribute("clase", "warning");
        repEstudiante.deleteById(estudiante.getId());
        return "estudiante";
    }
}