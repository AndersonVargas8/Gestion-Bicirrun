package com.app.springapp.service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.app.springapp.dto.Calendario;
import com.app.springapp.entity.Horario;
import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.repository.CupoRepository;
import com.app.springapp.repository.HorarioRepository;
import com.app.springapp.repository.TurnoRepository;

@Lazy
@Service
public class HorarioService implements IServicioHorario{
    @Autowired
    HorarioRepository repHorario;

    @Autowired
    TurnoService serTurno;

    @Autowired
    CupoRepository repCupo;

    @Autowired
    TurnoRepository repTurno;

    @Override
    public int guardarHorario(Horario horario) {
        repHorario.save(horario);
        return 0;
    }

    @Override
    public List<Horario> obtenerTodos() {
        return (List<Horario>)repHorario.findAll();
    }

    @Override
    public Horario buscarPorId(int id) {
        return repHorario.findById(new Long(id)).get();
    }

    @Override
    public List<Horario> obtenerDisponiblesPorFecha(LocalDate fecha) {
        int dia = fecha.getDayOfMonth(), mes = fecha.getMonthValue(), anio = fecha.getYear();

        // Se obtienen los cupos en cada horario
        Map<Long, Integer> cuposHorarios = repCupo.sumCuposGroupByHorario();
        // Se obtiene los horarios dependientes
        Map<Long, Long> horariosDependientes = repHorario.findHorariosDependientes();
        // Se obtienen los turnos progaramados en cada horario dada la fecha
        Map<Long, Integer> turnosHorarios = repTurno.sumAllTurnosByDiaAndMesAndAnio(dia, mes, anio);
        // Se obtienen todos los horarios
        List<Horario> listaHorarios = obtenerTodos();

        List<Horario> horariosDisponibles = new ArrayList<>();

        int valorDiaSemana = fecha.get(WeekFields.ISO.dayOfWeek());
        String nombreDia = Calendario.convertirNumeroADia(valorDiaSemana).toLowerCase();

        Map<Long, Long> horariosIndependientes = new HashMap<>();
        for(Map.Entry<Long, Long> entry: horariosDependientes.entrySet()){
            horariosIndependientes.put(entry.getValue(), entry.getKey());
        }

        for(Horario horario: listaHorarios){
            if(horario.diaNoDisponible(nombreDia))
                continue;

            int turnos = 0;
            if(turnosHorarios.containsKey(horario.getId()))
                turnos = turnosHorarios.get(horario.getId());

            int cupos = cuposHorarios.get(horario.getId());

            if(horariosDependientes.containsKey(horario.getId())){
                cupos = cuposHorarios.get(horariosDependientes.get(horario.getId()));

                long horarioIndependiente = horariosDependientes.get(horario.getId());
                if(turnosHorarios.containsKey(horarioIndependiente))
                    turnos += turnosHorarios.get(horarioIndependiente);
            }

            if(horariosIndependientes.containsKey(horario.getId())){
                long horarioDependiente = horariosIndependientes.get(horario.getId());
                if(turnosHorarios.containsKey(horarioDependiente))
                    turnos += turnosHorarios.get(horarioDependiente);
            }

            if(turnos < cupos){
                horariosDisponibles.add(horario);
            };
        }
        return horariosDisponibles;
    }

    
}
