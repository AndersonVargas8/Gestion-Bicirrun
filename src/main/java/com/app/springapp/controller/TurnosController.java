package com.app.springapp.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Disponibilidad;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;
import com.app.springapp.entity.Turno;
import com.app.springapp.repository.CupoRepository;
import com.app.springapp.repository.EstadoTurnoRepository;
import com.app.springapp.repository.EstudianteRepository;
import com.app.springapp.repository.TurnoRepository;
import com.app.springapp.service.DisponibilidadService;
import com.app.springapp.service.EstacionService;
import com.app.springapp.service.HorarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TurnosController {
    @Autowired
    HorarioService serHorario;

    @Autowired
    EstacionService serEstacion;

    @Autowired
    EstudianteRepository repEstudiante;

    @Autowired
    EstadoTurnoRepository repEstadoTurno;

    @Autowired
    CupoRepository repCupo;

    @Autowired
    DisponibilidadService serDisponibilidad;

    @Autowired
    TurnoRepository repTurno;

    private List<String> meses;

    @GetMapping("/turnos")
    public String index(ModelMap model) {
        model = addAttributesTurnos(model);

        model.addAttribute("dias", generarDiasHabiles(0));
        return "turnos";
    }

    @PostMapping("/turnos")
    public String createUser(@Valid @ModelAttribute("turno") Turno turno, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            model = addAttributesTurnos(model);

            model.addAttribute("dias", generarDiasHabiles(0));
            model.addAttribute("nuevoTurno", new Turno());
            return "turno";
        } else {
            try {
                repTurno.save(turno);
                Cupo cupo = repCupo.findByEstacionAndHorario(turno.getEstacion(), turno.getHorario()).get();
                if (cupo.getCupoGrupo() != 0)// Verifica si el Cupo comparte número de cupos con otro Cupo (si es 0, no
                                             // tiene cupoGrupo)
                    cupo = repCupo.findById(new Long(cupo.getCupoGrupo())); // asigna el cupoGrupo al cupo

                Disponibilidad disponibilidad = serDisponibilidad.consultarDisponibilidadMesDia(turno.getMes(),
                        turno.getDia(), cupo);

                if (disponibilidad == null) {
                    // Actualiza los registros de disponibilidad
                    serDisponibilidad.actualizarCuposDia(turno.getDia(), turno.getMes());
                    // Carga el registro de disponibilidad
                    disponibilidad = serDisponibilidad.consultarDisponibilidadMesDia(turno.getMes(),
                            turno.getDia(), cupo);
                }

                disponibilidad.setNum_disponibles(disponibilidad.getNum_disponibles() - 1);
                serDisponibilidad.guardarDisponibilidad(disponibilidad);
            } catch (Exception e) {
                model.addAttribute("error", "Error: " + e.getMessage());
            }
        }

        model = addAttributesTurnos(model);
        model.addAttribute("dias", generarDiasHabiles(0));
        return "turnos";
    }

    @GetMapping("/actFormTurnosMes/{mes}")
    public String actFormTurnosMes(ModelMap model, @PathVariable int mes) {
        model = addAttributesTurnos(model);
        model.addAttribute("dias", generarDiasHabiles(mes));
        model.addAttribute("mesSel", getMes(mes));

        model.addAttribute("nuevoTurno", new Turno());
        return "formTurnos";
    }

    @GetMapping("/actFormTurnosDiaMes/{dia}/{mes}")
    public String actFormTurnosDiaMes(ModelMap model, @PathVariable int dia, @PathVariable int mes) {
        model = addAttributesTurnos(model);
        model.addAttribute("dias", generarDiasHabiles(mes));
        model.addAttribute("mesSel", getMes(mes));
        model.addAttribute("horarios", getHorarios(dia, mes));
        model.addAttribute("diaSel", getDia(dia, mes));

        return "formTurnos";
    }

    @GetMapping("/actFormTurnosDiaMesHorario/{dia}/{mes}/{idHorario}")
    public String actFormTurnosDiaMesHorario(ModelMap model, @PathVariable int dia, @PathVariable int mes,
            @PathVariable int idHorario) {
        model = addAttributesTurnos(model);
        if(dia == 0){
            model.addAttribute("dias", generarDiasDisponibles(mes,idHorario));
        }else{       
            model.addAttribute("dias", generarDiasHabiles(mes));
        }
        model.addAttribute("mesSel", getMes(mes));
        model.addAttribute("diaSel", getDia(dia, mes));
        model.addAttribute("horSel", getHorario(idHorario));
        model.addAttribute("horarios", getHorarios(dia, mes));
        model.addAttribute("estaciones", getEstaciones(dia, mes, idHorario));

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

    private ModelMap addAttributesTurnos(ModelMap model) {
        model.addAttribute("listTab", "turnos");
        model.addAttribute("horarios", serHorario.obtenerTodos());
        model.addAttribute("estaciones", serEstacion.obtenerTodas());
        model.addAttribute("estudiantes", repEstudiante.findAll());
        model.addAttribute("estadoTurnos", repEstadoTurno.findAll());
        model.addAttribute("turnos", repTurno.findAll());
        model.addAttribute("meses", generarMeses());
        model.addAttribute("mesSel", getMes(0));
        model.addAttribute("diaSel", getDia(0, 0));
        model.addAttribute("horSel", getHorario(0));

        model.addAttribute("nuevoTurno", new Turno());

        return model;
    }

    private HashMap<Integer, String> generarDiasHabiles(int mesActual) {
        if (mesActual == 0) {
            Month mesActualClase = LocalDate.now().getMonth();
            mesActual = mesActualClase.getValue();
        }
        int anioActual = LocalDate.now().getYear();
        LocalDate fechaActual = LocalDate.of(anioActual, mesActual, 1);
        int diasDelMes = fechaActual.lengthOfMonth();
        int valorDiaActual = DayOfWeek.from(fechaActual).getValue();
        HashMap<Integer, String> dias = new HashMap<>();

        for (int i = 1; i <= diasDelMes; i++) {
            if (valorDiaActual % 7 != 6 && valorDiaActual % 7 != 0) {
                Integer num_disponibles = serDisponibilidad.cuposDisponiblesEnDia(i, mesActual);
                if (num_disponibles == null || num_disponibles > 0) {
                    String nombreDiaActual = DayOfWeek.of(valorDiaActual % 7).getDisplayName(TextStyle.FULL,
                            new Locale("es", "ES"));
                    dias.put(i, nombreDiaActual.substring(0, 1).toUpperCase() + nombreDiaActual.substring(1));

                }
            }
            valorDiaActual++;
        }

        return dias;
    }

    private HashMap<Integer, String> generarDiasDisponibles(int mes, int idHorario) {
        HashMap<Integer,String> diasHabiles = this.generarDiasHabiles(mes);
        List<Integer> diasOcupados = serDisponibilidad.diasSinDisponibilidadEnHorario(mes, idHorario);

        for(Integer dia: diasOcupados){
            diasHabiles.remove(dia);
        }
        return diasHabiles;
    }

    private HashMap<Integer, String> getMes(int mesValor) {

        Month mes = (mesValor == 0) ? LocalDate.now().getMonth() : Month.of(mesValor);
        String nombre = mes.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        HashMap<Integer, String> mesSel = new HashMap<>();
        mesSel.put(mes.getValue(), nombre.substring(0, 1).toUpperCase() + nombre.substring(1));
        return mesSel;
    }

    private HashMap<Integer, String> getDia(int dia, int mesValor) {
        int anioActual = LocalDate.now().getYear();
        int diaValor = (dia == 0) ? 1 : dia;
        Month mes = (mesValor == 0) ? LocalDate.now().getMonth() : Month.of(mesValor);

        LocalDate fechaActual = LocalDate.of(anioActual, mes.getValue(), diaValor);

        int valorDiaActual = DayOfWeek.from(fechaActual).getValue();

        String diaNombre = (dia == 0) ? "Seleccione"
                : DayOfWeek.of(valorDiaActual % 7).getDisplayName(TextStyle.FULL,
                        new Locale("es", "ES"));

        HashMap<Integer, String> diaSel = new HashMap<>();
        diaSel.put(dia, diaNombre.substring(0, 1).toUpperCase() + diaNombre.substring(1));

        return diaSel;
    }

    private HashMap<Integer, String> getHorario(int idHorario) {
        HashMap<Integer, String> horSel = new HashMap<>();
        if (idHorario == 0) {
            horSel.put(0, "Seleccione");
        } else {
            Horario horario = serHorario.buscarPorId(idHorario);
            horSel.put((int) horario.getId(), horario.getDescripcion());
        }

        return horSel;
    }

    private List<Horario> getHorarios(int dia, int mes) {
        List<Horario> horarios = serDisponibilidad.horariosDisponiblesDiaMes(dia, mes);
        return horarios;
    }

    private List<Estacion> getEstaciones(int dia, int mes, int idHorario) {
        List<Estacion> estaciones = serDisponibilidad.estacionesDisponiblesDiaMesHorario(dia, mes, idHorario);
        return estaciones;
    }
}
