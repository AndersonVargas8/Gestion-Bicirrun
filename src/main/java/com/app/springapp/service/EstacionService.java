package com.app.springapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.app.springapp.dto.CuposYTurnosEstacionesHorario;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;
import com.app.springapp.interfacesServicios.IServicioCupo;
import com.app.springapp.interfacesServicios.IServicioEstacion;
import com.app.springapp.interfacesServicios.IServicioTurno;
import com.app.springapp.repository.CupoRepository;
import com.app.springapp.repository.EstacionRepository;
import com.app.springapp.repository.HorarioRepository;
import com.app.springapp.repository.TurnoRepository;

@Lazy
@Service
public class EstacionService implements IServicioEstacion {
    @Autowired
    EstacionRepository repEstacion;

    @Autowired
    IServicioCupo serCupo;

    @Autowired
    IServicioTurno serTurno;

    @Autowired
    CupoRepository repCupo;
    
    @Autowired
    TurnoRepository repTurno;

    @Autowired
    HorarioRepository repHorario;

    @Override
    public List<Estacion> obtenerTodas() {
        return (List<Estacion>) repEstacion.findAllByOrderByIdAsc();
    }

    @Override
    public Estacion buscarPorId(int id) {
        return repEstacion.findById(new Long(id)).get();
    }

    @Override
    public List<Estacion> obtenerDisponiblesPorFechaYHorario(LocalDate fecha, Horario horario) {
        int dia = fecha.getDayOfMonth(), mes = fecha.getMonthValue(), anio = fecha.getYear();

        CuposYTurnosEstacionesHorario cuposTurnos = new CuposYTurnosEstacionesHorario();
        // Se obtienen los cupos de las estaciones en cada horario
        cuposTurnos = repCupo.sumCuposGroupByEstacionHorario(cuposTurnos);
        // Se obtienen los turnos de las estaciones en cada horario
        cuposTurnos = repTurno.sumTurnosGroupByEstacionHorarioFecha(cuposTurnos, dia, mes, anio);
        // Se obtiene los horarios dependientes
        Map<Long, Long> horariosDependientes = repHorario.findHorariosDependientes();
        // Se obtienen todas las estaciones
        List<Estacion> listaEstaciones = obtenerTodas();

        List<Estacion> estacionesDisponibles = new ArrayList<>();

        Map<Long, Long> horariosIndependientes = new HashMap<>();
        for(Map.Entry<Long, Long> entry: horariosDependientes.entrySet()){
            horariosIndependientes.put(entry.getValue(), entry.getKey());
        }
        
        for (Estacion estacion : listaEstaciones) {
            Integer cupos = cuposTurnos.getCupos(estacion.getId(), horario.getId());
            Integer turnos = cuposTurnos.getTurnos(estacion.getId(), horario.getId());

            if(horariosDependientes.containsKey(horario.getId())){
                long horarioIndependiente = horariosDependientes.get(horario.getId());
                cupos += cuposTurnos.getCupos(estacion.getId(), horarioIndependiente);
                turnos += cuposTurnos.getTurnos(estacion.getId(), horarioIndependiente);
            }

            if(horariosIndependientes.containsKey(horario.getId())){
                long horarioDependiente = horariosIndependientes.get(horario.getId());
                turnos += cuposTurnos.getTurnos(estacion.getId(), horarioDependiente);
            }

            if(cupos == 0){
                continue;
            }
            
            if (turnos < cupos) {
                estacionesDisponibles.add(estacion);
            }
        }
        return estacionesDisponibles;
    }

}
