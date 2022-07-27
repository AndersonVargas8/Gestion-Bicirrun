package com.app.springapp.service;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.springapp.dto.Calendario;
import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Turno;
import com.app.springapp.interfacesServicios.IServicioCupo;
import com.app.springapp.interfacesServicios.IServicioTurno;
import com.app.springapp.model.Mes;
import com.app.springapp.repository.TurnoRepository;

@Service
public class TurnoService implements IServicioTurno{
    @Autowired
    TurnoRepository repTurno;

    @Autowired
    IServicioCupo serCupo;

    @Override
    public Turno obtenerPorId(long id){
        return repTurno.findById(id).get();
    }

    @Override
    public List<Turno> obtenerTodos() {
        return (List<Turno>)repTurno.findAll();
    }

    @Override
    public List<Turno> obtenerPorDiaMes(int dia, int mes) {
        List<Turno> respuesta = repTurno.findByDiaAndMes(dia, mes);
        if(respuesta.isEmpty())
            return null;

        return respuesta;
    }

    @Override
    public int guardarTurno(Turno turno) {
        repTurno.save(turno);
        return 0;
    }

    @Override
    public List<Turno> obtenerPorEstudiante(Estudiante estudiante) {
        List<Turno> respuesta = repTurno.findByEstudiante(estudiante);
        Collections.sort(respuesta);
        return respuesta;
    }

    @Override
    public void eliminarTurno(Turno turno) {
        repTurno.delete(turno);
    }

    /**
     * @param mes
     * @param anio
     * @return
     */
    public Calendario getCalendario(int mes, int anio){
        // Obtener la fecha con el mes y año dado
        LocalDate fecha= LocalDate.of(anio, mes, 1);
        int diasDelMes = fecha.lengthOfMonth();

        Calendario calendario = new Calendario(mes, anio);

        // Objeto Mes para buscar los turnos programados
        Mes mesTurnosPro = repTurno.getMesTurnosProgramados(anio, calendario.nombreMes);

        // HashMap con los cupos por día
        HashMap<String,Integer> cuposPorDia = serCupo.cantidadCuposAlDia();

        int numeroSemanasGuardadas = 0;
        

        for (int diaMes = 1; diaMes <= diasDelMes; diaMes++) {
            LocalDate dia = fecha.withDayOfMonth(diaMes);
            int valorDiaActual = DayOfWeek.from(dia).getValue();
            String nombreDia = Calendario.convertirNumeroADia(valorDiaActual);

            if (valorDiaActual <= 5) {// Si el día no es sábado ni domingo
                int cuposDia = cuposPorDia.get(nombreDia);
                int turnosProgramados = mesTurnosPro.numeroTurnosDia(diaMes);
                calendario.agregarDiaSemana(numeroSemanasGuardadas, valorDiaActual, diaMes, cuposDia, turnosProgramados);
                
                if(valorDiaActual == 5){
                    numeroSemanasGuardadas++;
                }
            }
        }

        return calendario;
    }

}
