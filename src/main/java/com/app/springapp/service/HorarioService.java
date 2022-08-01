package com.app.springapp.service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.app.springapp.dto.Calendario;
import com.app.springapp.entity.Horario;
import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.interfacesServicios.IServicioTurno;
import com.app.springapp.repository.HorarioRepository;

@Lazy
@Service
public class HorarioService implements IServicioHorario{
    @Autowired
    HorarioRepository repHorario;

    @Autowired
    IServicioTurno serTurno;

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
        List<Horario> listaHorarios = obtenerTodos();
        List<Horario> horariosDisponibles = new ArrayList<>();
        int valorDiaSemana = fecha.get(WeekFields.ISO.dayOfWeek());
        String nombreDia = Calendario.convertirNumeroADia(valorDiaSemana).toLowerCase();
        for(Horario horario: listaHorarios){
            if(horario.diaNoDisponible(nombreDia)){
                continue;
            }
            if(serTurno.hayTurnosDisponibles(fecha, horario)){
                horariosDisponibles.add(horario);
            };
        }
        return horariosDisponibles;
    }

    
}
