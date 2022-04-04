package com.app.springapp.controller;

import javax.validation.Valid;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.entity.Turno;
import com.app.springapp.repository.EstacionRepository;
import com.app.springapp.repository.EstadoTurnoRepository;
import com.app.springapp.repository.EstudianteRepository;
import com.app.springapp.repository.HorarioRepository;
import com.app.springapp.repository.TurnoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @Autowired
    TurnoRepository repTurno;

    @GetMapping("/turnos")
    public String index(Model model) {
        model.addAttribute("horarios", repHorario.findAll());
        model.addAttribute("estaciones", repEstacion.findAll());
        model.addAttribute("estudiantes", repEstudiante.findAll());
        model.addAttribute("estadoTurnos", repEstadoTurno.findAll());
        model.addAttribute("turnos",repTurno.findAll());
        
        model.addAttribute("nuevoTurno", new Turno());
        return "turnos";
    }

    @PostMapping("/turnos")
    public String createUser(@Valid @ModelAttribute("turno") Turno turno, BindingResult result, ModelMap model) {
        
        //repTurno.save(turno);
        if (result.hasErrors()) {
            model.addAttribute("prueba", repHorario.findAll());
            model.addAttribute("prueba2", repEstacion.findAll());
            model.addAttribute("prueba3", repEstudiante.findAll());
            model.addAttribute("prueba4", repEstadoTurno.findAll());
        
            model.addAttribute("turno", new Turno());
            return "pruebas";
        }else {
            try {
                String[] fecha = turno.getFecha().split("-");
                turno.setDia(Integer.parseInt(fecha[2]));
                turno.setMes(Integer.parseInt(fecha[1]));
                
                repTurno.save(turno);
            } 
            catch (Exception e) {
                model.addAttribute("prueba", e.getMessage());
            }
        }

        model.addAttribute("horarios", repHorario.findAll());
        model.addAttribute("estaciones", repEstacion.findAll());
        model.addAttribute("estudiantes", repEstudiante.findAll());
        model.addAttribute("estadoTurnos", repEstadoTurno.findAll());
        model.addAttribute("turnos",repTurno.findAll());
        
        model.addAttribute("nuevoTurno", new Turno());
        return "turnos";
    }
}
