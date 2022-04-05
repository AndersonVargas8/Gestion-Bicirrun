package com.app.springapp.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.PathVariable;
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

    private List<String> meses;

    @GetMapping("/turnos")
    public String index(ModelMap model) {
        model = addAttributesTurnos(model);
        
        return "turnos";
    }

    @PostMapping("/turnos")
    public String createUser(@Valid @ModelAttribute("turno") Turno turno, BindingResult result, ModelMap model) {

        // repTurno.save(turno);
        if (result.hasErrors()) {     
            model = addAttributesTurnos(model);

            model.addAttribute("nuevoTurno", new Turno());
            return "turno";
        } else {
            try {
                repTurno.save(turno);
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        model = addAttributesTurnos(model);
        return "turnos";
    }

    @GetMapping("/actFormTurnos/{mes}")
    public String reporteCompras(ModelMap model, @PathVariable int mes) {
        model = addAttributesTurnos(model);
        model.addAttribute("dias",generarDias(mes));
        model.addAttribute("mesSel",getMes(mes));
        
        model.addAttribute("nuevoTurno", new Turno());
        return "formTurnos";
    }

    private List<String> generarMeses() {
        if (meses != null)
            return meses;
        meses = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Month mes = Month.of(i);
            String nombre = mes.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
            nombre = nombre.substring(0, 1).toUpperCase() + nombre.substring(1);
            meses.add(nombre);
        }

        return meses;

    }

    private ModelMap addAttributesTurnos(ModelMap model){
        model.addAttribute("listTab", "turnos");
        model.addAttribute("horarios", repHorario.findAll());
        model.addAttribute("estaciones", repEstacion.findAll());
        model.addAttribute("estudiantes", repEstudiante.findAll());
        model.addAttribute("estadoTurnos", repEstadoTurno.findAll());
        model.addAttribute("turnos", repTurno.findAll());
        model.addAttribute("meses", generarMeses());
        model.addAttribute("dias", generarDias(0));
        model.addAttribute("mesSel",getMes(0));

        Turno turno = new Turno();
        model.addAttribute("nuevoTurno", new Turno());

        return model;
    }

    private HashMap<Integer,String> generarDias(int mesActual){
        if(mesActual == 0){
            Month mesActualValor = LocalDate.now().getMonth();
            mesActual = mesActualValor.getValue();
        }
        int anioActual = LocalDate.now().getYear();
        LocalDate fechaActual = LocalDate.of(anioActual,mesActual,1);
        int diasDelMes = fechaActual.lengthOfMonth();
		int valorDiaActual = DayOfWeek.from(fechaActual).getValue();
		HashMap<Integer,String> dias = new HashMap<>(); 

		for(int i = 1; i <= diasDelMes; i++){
			if(valorDiaActual % 7 != 6 && valorDiaActual % 7 != 0){
				String nombreDiaActual = DayOfWeek.of(valorDiaActual%7).getDisplayName(TextStyle.FULL, new Locale("es","ES"));
				dias.put(i,nombreDiaActual.substring(0, 1).toUpperCase() + nombreDiaActual.substring(1));
			}
			valorDiaActual++;
		}
        
        return dias;
    }

    private HashMap<Integer,String> getMes(int mesValor){
        
        Month mes = (mesValor == 0) ? LocalDate.now().getMonth() : Month.of(mesValor);
        String nombre = mes.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        HashMap<Integer,String> mesSel = new HashMap<>();
        mesSel.put(mes.getValue(), nombre.substring(0, 1).toUpperCase() + nombre.substring(1));
        return mesSel;
    }
}
