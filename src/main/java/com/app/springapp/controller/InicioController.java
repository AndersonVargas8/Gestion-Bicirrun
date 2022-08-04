package com.app.springapp.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.interfacesServicios.IServicioTurno;
import com.app.springapp.repository.EstudianteRepository;

@Controller
@RequestMapping("/inicio")
public class InicioController {
    @Autowired
    EstudianteRepository repEstudiante;

    @Autowired
    IServicioTurno serTurno;
    
    @Autowired
    IServicioHorario serHorario;

    @GetMapping
    public String inicio(ModelMap model) {
        model.addAttribute("horarios", serHorario.obtenerTodos());
        model.addAttribute("estudiantes", repEstudiante.findAll());
        model.addAttribute("calendario",serTurno.getCalendario(LocalDate.now().getMonthValue(), LocalDate.now().getYear()));
        return "turnos/turnos";
    }
}
