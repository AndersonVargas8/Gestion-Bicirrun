package com.app.springapp.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import com.app.springapp.entity.Cupo;
//import com.app.springapp.entity.Disponibilidad;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;
import com.app.springapp.entity.Turno;
import com.app.springapp.model.TurnosProgramados;
import com.app.springapp.repository.EstadoTurnoRepository;
import com.app.springapp.repository.EstudianteRepository;
import com.app.springapp.repository.TurnoRepository;
import com.app.springapp.service.CupoService;
//import com.app.springapp.service.DisponibilidadService;
import com.app.springapp.service.EstacionService;
import com.app.springapp.service.HorarioService;
import com.app.springapp.service.TurnoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    CupoService serCupo;


    @Autowired
    TurnoService serTurno;

    @Autowired
    TurnoRepository repTurnos;

    private List<String> meses;

    @GetMapping("/turnos")
    @ResponseBody
    public TurnosProgramados index(ModelMap model) {
        /*model.addAttribute("infoCalendar", informacionCalendar(0));
        model.addAttribute("mesCalendar", getMesCalendar(0, 0));
        model = addAttributesTurnos(model);

        model.addAttribute("dias", generarDiasHabiles(0));*/
        TurnosProgramados t = repTurnos.getTurnosProgramados(2022);
        return t;
    }

    @PostMapping("/turnos")
    public String crearTurno(@Valid @ModelAttribute("turno") Turno turno, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            model = addAttributesTurnos(model);

            model.addAttribute("infoCalendar", informacionCalendar(0));
            model.addAttribute("mesCalendar", getMesCalendar(0, 0));
            model.addAttribute("dias", generarDiasHabiles(0));
            model.addAttribute("nuevoTurno", new Turno());
            return "turno";
        } else {
            try {
                serTurno.guardarTurno(turno);
                Cupo cupo = serCupo.buscarPorEstacionYHorario(turno.getEstacion(), turno.getHorario());
                /*if (cupo.getCupoGrupo() != 0)// Verifica si el Cupo comparte número de cupos con otro Cupo (si es 0, no
                                             // tiene cupoGrupo)
                    cupo = serCupo.buscarPorId(cupo.getCupoGrupo()); // asigna el cupoGrupo al cupo

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
                serDisponibilidad.guardarDisponibilidad(disponibilidad);*/
            } catch (Exception e) {
                model.addAttribute("error", "Error: " + e.getMessage());
            }
        }

        model.addAttribute("infoCalendar", informacionCalendar(turno.getMes()));
        model.addAttribute("mesCalendar", getMesCalendar(0, turno.getMes()));
        model = addAttributesTurnos(model);
        model.addAttribute("dias", generarDiasHabiles(0));
        return "redirect:/turnos";
    }

    @GetMapping("/eliminarTurno/{id}")
    public String eliminarTurno(@PathVariable int id, ModelMap model) {
        Turno turno = serTurno.obtenerPorId(id);
        serTurno.eliminarTurno(turno);
        Cupo cupo = serCupo.buscarPorEstacionYHorario(turno.getEstacion(), turno.getHorario());
        /*if (cupo.getCupoGrupo() != 0)// Verifica si el Cupo comparte número de cupos con otro Cupo (si es 0, no
                                     // tiene cupoGrupo)
            cupo = serCupo.buscarPorId(cupo.getCupoGrupo()); // asigna el cupoGrupo al cupo

        Disponibilidad disponibilidad = serDisponibilidad.consultarDisponibilidadMesDia(turno.getMes(),
                turno.getDia(), cupo);
        disponibilidad.setNum_disponibles(disponibilidad.getNum_disponibles() + 1);
        serDisponibilidad.guardarDisponibilidad(disponibilidad);*/
        return "redirect:/turnos";
    }

    @GetMapping("/actFormTurnosMes/{mes}")
    public String actFormTurnosMes(ModelMap model, @PathVariable int mes) {
        model = addAttributesTurnos(model);
        model.addAttribute("dias", generarDiasHabiles(mes));
        model.addAttribute("mesSel", getMes(mes));

        model.addAttribute("nuevoTurno", new Turno());
        return "turnos/formTurnos";
    }

    @GetMapping("/actFormTurnosDiaMes/{dia}/{mes}/{idHorario}")
    public String actFormTurnosDiaMes(ModelMap model, @PathVariable int dia, @PathVariable int mes,
            @PathVariable int idHorario) {
        model = addAttributesTurnos(model);
        model.addAttribute("dias", generarDiasHabiles(mes));
        model.addAttribute("mesSel", getMes(mes));
        //model.addAttribute("horarios", getHorarios(dia, mes));
        model.addAttribute("diaSel", getDia(dia, mes));

        if (idHorario != 0)
            model.addAttribute("horSel", getHorario(idHorario));

        return "turnos/formTurnos";
    }

    @GetMapping("/actFormTurnosDiaMesHorario/{dia}/{mes}/{idHorario}")
    public String actFormTurnosDiaMesHorario(ModelMap model, @PathVariable int dia, @PathVariable int mes,
            @PathVariable int idHorario) {
        model = addAttributesTurnos(model);
        if (dia == 0) {
            model.addAttribute("dias", generarDiasDisponibles(mes, idHorario));
        } else {
            model.addAttribute("dias", generarDiasHabiles(mes));
        }
        model.addAttribute("mesSel", getMes(mes));
        model.addAttribute("diaSel", getDia(dia, mes));
        model.addAttribute("horSel", getHorario(idHorario));
        //model.addAttribute("horarios", getHorarios(dia, mes));
       // model.addAttribute("estaciones", getEstaciones(dia, mes, idHorario));

        return "turnos/formTurnos";
    }

    @GetMapping("/crearTurnoDefinido/{mes}/{dia}/{idHorario}/{idEstacion}")
    public String crearTurnoDefinido(@PathVariable int mes, @PathVariable int dia, @PathVariable int idHorario,
            @PathVariable int idEstacion, ModelMap model) {
        model = addAttributesTurnos(model);
        
        model.addAttribute("dias", generarDiasHabiles(0));
        model.addAttribute("mesSel", getMes(mes));
        model.addAttribute("diaSel", getDia(dia, mes));
        model.addAttribute("horSel", getHorario(idHorario));
        model.addAttribute("estacionSel", serEstacion.buscarPorId(idEstacion));
        return "turnos/formTurnos";
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
        model.addAttribute("turnos", serTurno.obtenerTodos());
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
                /*Integer num_disponibles = serDisponibilidad.cuposDisponiblesEnDia(i, mesActual);
                if (num_disponibles == null || num_disponibles > 0) {
                    String nombreDiaActual = DayOfWeek.of(valorDiaActual % 7).getDisplayName(TextStyle.FULL,
                            new Locale("es", "ES"));
                    dias.put(i, nombreDiaActual.substring(0, 1).toUpperCase() + nombreDiaActual.substring(1));

                }*/
            }
            valorDiaActual++;
        }

        return dias;
    }

    private HashMap<Integer, String> generarDiasDisponibles(int mes, int idHorario) {
        HashMap<Integer, String> diasHabiles = this.generarDiasHabiles(mes);
        /*List<Integer> diasOcupados = serDisponibilidad.diasSinDisponibilidadEnHorario(mes, idHorario);

        HashMap<Integer, String> diasHabilesCopy = (HashMap<Integer, String>) diasHabiles.clone();
        // Quitar los viernes si el horario es de 1PM
        if (idHorario == 4) {
            for (Integer diaHabil : diasHabilesCopy.keySet()) {
                if (diasHabilesCopy.get(diaHabil).equals("Viernes")) {
                    diasHabiles.remove(diaHabil);
                }
            }
        }

        for (Integer dia : diasOcupados) {
            diasHabiles.remove(dia);
        }*/
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

    /*private List<Horario> getHorarios(int dia, int mes) {
        List<Horario> horarios = serDisponibilidad.horariosDisponiblesDiaMes(dia, mes);
        return horarios;
    }

    private List<Estacion> getEstaciones(int dia, int mes, int idHorario) {
        List<Estacion> estaciones = serDisponibilidad.estacionesDisponiblesDiaMesHorario(dia, mes, idHorario);
        return estaciones;
    }*/

    private HashMap<Integer, HashMap<String, HashMap<Integer, Integer>>> informacionCalendar(int mesActual) {
        // Si el argumento es 0, entonces se toma el valor del mes como el mes actual
        if (mesActual == 0) {
            Month mesActualClase = LocalDate.now().getMonth();
            mesActual = mesActualClase.getValue();
        }
        // Obtener la fecha con un mes dado
        int anioActual = LocalDate.now().getYear();
        LocalDate fechaActual = LocalDate.of(anioActual, mesActual, 1);
        int diasDelMes = fechaActual.lengthOfMonth();

        // Obtener cantidad de semanas del mes
        LocalDate ultimoDia = fechaActual.withDayOfMonth(diasDelMes);
        int cantSemanas = ultimoDia.get(WeekFields.ISO.weekOfMonth()) + 1;

        /*
         * El HashMap contendrá n llaves las n semanas, cada llave contiene la el número
         * de semana y su valor
         * será un Hashmap con la info de cada día: [0] = lunes, [2] = martes,..., en
         * cada campo se almacena un
         * HashMap con el número de día como llave y la cantidad de cupos disponibles
         * como valor
         */
        HashMap<Integer, HashMap<String, HashMap<Integer, Integer>>> infoCalendario = new HashMap<>();

        for (int i = 1; i <= cantSemanas; i++) {
            HashMap<String, HashMap<Integer, Integer>> mapa = new HashMap<>();
            mapa.put("Lu", null);
            mapa.put("Ma", null);
            mapa.put("Mi", null);
            mapa.put("Ju", null);
            mapa.put("Vi", null);

            infoCalendario.put(i, mapa);
        }
        int valorDiaActual = DayOfWeek.from(fechaActual).getValue();
        Integer totalCupos = serCupo.cantidadCupos();

        for (int i = 1; i <= diasDelMes; i++) {
            if (valorDiaActual % 7 != 6 && valorDiaActual % 7 != 0) {// Si el día no es sábado ni domingo
                // Obtener la semana a la que pertenece
                LocalDate dia = fechaActual.withDayOfMonth(i);
                int numSemana = dia.get(WeekFields.ISO.weekOfMonth()) + 1;

                // Se obtiene el HashMap de la semana correspondiente
                HashMap<String, HashMap<Integer, Integer>> mapaSemana = infoCalendario.get(numSemana);

                // Se agrega la info del día con sus cupos disponibles
                HashMap<Integer, Integer> mapaDia = new HashMap<>();
                /*Integer num_disponibles = serDisponibilidad.cuposDisponiblesEnDia(i, mesActual);
                if (num_disponibles == null) {
                    if (valorDiaActual % 7 == 5)
                        num_disponibles = serCupo.cantidadCuposViernes();
                    else
                        num_disponibles = totalCupos;
                }

                mapaDia.put(i, num_disponibles);*/

                // asignar el mapa Día al día correspondiente de la semana
                switch (valorDiaActual % 7) {
                    case 1:
                        mapaSemana.put("Lu", mapaDia);
                        break;
                    case 2:
                        mapaSemana.put("Ma", mapaDia);
                        break;
                    case 3:
                        mapaSemana.put("Mi", mapaDia);
                        break;
                    case 4:
                        mapaSemana.put("Ju", mapaDia);
                        break;
                    case 5:
                        mapaSemana.put("Vi", mapaDia);
                        break;

                }

                infoCalendario.put(numSemana, mapaSemana);
            }
            valorDiaActual++;
        }

        return infoCalendario;
    }

    private String getMesCalendar(int anio, int mes) {
        mes = (mes == 0) ? LocalDate.now().getMonth().getValue() : mes;
        anio = (anio == 0) ? LocalDate.now().getYear() : anio;

        String mesCalendar = Integer.toString(anio) + "-";

        if (mes < 10)
            mesCalendar = mesCalendar.concat("0");

        mesCalendar = mesCalendar.concat(Integer.toString(mes));

        return mesCalendar;
    }

    @GetMapping("/actCalendario/{mes}")
    public String actFormTurnosMes(ModelMap model, @PathVariable String mes) {
        String[] datos = mes.split("-");
        model.addAttribute("infoCalendar", informacionCalendar(Integer.parseInt(datos[1])));
        model.addAttribute("mesCalendar", getMesCalendar(Integer.parseInt(datos[0]), Integer.parseInt(datos[1])));
        return "turnos/calendario";
    }

    @GetMapping("/turnosEstacionesDia/{dia}/{mesAnio}")
    public String turnosEstacionesDia(ModelMap model, @PathVariable int dia, @PathVariable String mesAnio) {

        String[] datos = mesAnio.split("-");
        int mes = Integer.parseInt(datos[1]);

        model.addAttribute("diaSelCal", dia);
        model.addAttribute("mesSelCal", mes);
        HashMap<Integer, HashMap<Integer, List<Turno>>> totalEstaciones = new HashMap<>();

        List<Estacion> estaciones = serEstacion.obtenerTodas();
        List<Horario> horarios = serHorario.obtenerTodos();

        HashMap<Integer, List<Turno>> mapHorarios = new HashMap<>();
        for (Horario horario : horarios) {
            mapHorarios.put((int) horario.getId(), null);
        }

        for (Estacion estacion : estaciones) {
            totalEstaciones.put((int) estacion.getId(), (HashMap<Integer, List<Turno>>) mapHorarios.clone());
        }
        model = disponibilidadEstacionesDia(model, dia, mes);

        List<Turno> turnos = serTurno.obtenerPorDiaMes(dia, mes);
        if (turnos == null) {
            model.addAttribute("totalEstaciones", totalEstaciones);
            return "turnos/turnosEstaciones";
        }

        for (Turno turno : turnos) {
            HashMap<Integer, List<Turno>> mapEstacion = totalEstaciones.get((int) turno.getEstacion().getId());
            List<Turno> listaTurnos = mapEstacion.get((int) turno.getHorario().getId());
            if (listaTurnos == null) {
                listaTurnos = new ArrayList<>();
            }
            listaTurnos.add(turno);
            mapEstacion.put((int) turno.getHorario().getId(), listaTurnos);
            totalEstaciones.put((int) turno.getEstacion().getId(), mapEstacion);
        }

        model.addAttribute("totalEstaciones", totalEstaciones);
        return "turnos/turnosEstaciones";
    }

    public ModelMap disponibilidadEstacionesDia(ModelMap model, int dia, int mes) {

        HashMap<Integer, HashMap<Integer, List<Integer>>> totalDispEstaciones = new HashMap<>();

        List<Estacion> estaciones = serEstacion.obtenerTodas();
        List<Horario> horarios = serHorario.obtenerTodos();

        HashMap<Integer, List<Integer>> mapHorarios = new HashMap<>();
        for (Horario horario : horarios) {
            mapHorarios.put((int) horario.getId(), null);
        }

        for (Estacion estacion : estaciones) {
            totalDispEstaciones.put((int) estacion.getId(), (HashMap<Integer, List<Integer>>) mapHorarios.clone());
        }

        /*List<Disponibilidad> disponibilidades = serDisponibilidad.obtenerTodasPorDiaMes(dia, mes);

        if (disponibilidades == null || disponibilidades.size() == 0) {
            serDisponibilidad.actualizarCuposDia(dia, mes);
            disponibilidades = serDisponibilidad.obtenerTodasPorDiaMes(dia, mes);
            // model.addAttribute("totalDispEstaciones", totalDispEstaciones);
            // return model;
        }

        for (Disponibilidad disponibilidad : disponibilidades) {
            HashMap<Integer, List<Integer>> mapEstacion = totalDispEstaciones
                    .get((int) disponibilidad.getCupo().getEstacion().getId());

            List<Integer> listaTurnos = mapEstacion.get((int) disponibilidad.getCupo().getHorario().getId());
            if (listaTurnos == null) {
                listaTurnos = new ArrayList<>();
            }
            for (int i = 0; i < disponibilidad.getNum_disponibles(); i++)
                listaTurnos.add(1);

            mapEstacion.put((int) disponibilidad.getCupo().getHorario().getId(), listaTurnos);
            totalDispEstaciones.put((int) disponibilidad.getCupo().getEstacion().getId(), mapEstacion);
        }*/

        model.addAttribute("totalDispEstaciones", totalDispEstaciones);
        return model;
    }

}
