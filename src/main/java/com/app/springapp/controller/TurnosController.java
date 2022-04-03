package com.app.springapp.controller;

import com.app.springapp.repository.EstacionRepository;
import com.app.springapp.repository.EstadoTurnoRepository;
import com.app.springapp.repository.EstudianteRepository;
import com.app.springapp.repository.HorarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TurnosController {
    @Autowired
    HorarioRepository repHorario;

    @Autowired
    EstacionRepository repEstacion;

    @Autowired
    EstudianteRepository repEstudiante;

    @Autowired
    EstadoTurnoRepository repEstadoTurno;

    @GetMapping("/prueba")
    public String index(Model model) {
        model.addAttribute("prueba", repHorario.findAll());
        model.addAttribute("prueba2", repEstacion.findAll());
        model.addAttribute("prueba3", repEstudiante.findAll());
        model.addAttribute("prueba4", repEstadoTurno.findAll());
        return "pruebas";
    }
}
