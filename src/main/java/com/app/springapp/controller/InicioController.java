package com.app.springapp.controller;

import java.time.LocalDate;

import com.app.springapp.service.EstudianteService;
import com.app.springapp.service.HorarioService;
import com.app.springapp.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.springapp.repository.EstudianteRepository;

@Controller
@RequestMapping("/inicio")
public class InicioController {
    @Autowired
    EstudianteService serEstudiante;

    @Autowired
    TurnoService serTurno;
    
    @Autowired
    HorarioService serHorario;

    @GetMapping
    public String inicio(ModelMap model) {
        model.addAttribute("horarios", serHorario.obtenerTodos());
        model.addAttribute("estudiantes", serEstudiante.obtenerIdYNombre());
        model.addAttribute("calendario",serTurno.getCalendario(LocalDate.now().getMonthValue(), LocalDate.now().getYear()));
        return "turnos/turnos";
    }
}
