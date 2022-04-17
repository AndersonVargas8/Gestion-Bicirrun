package com.app.springapp.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Disponibilidad;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;
import com.app.springapp.interfacesServicios.IServicioDisponibilidad;
import com.app.springapp.repository.DisponibilidadRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisponibilidadService implements IServicioDisponibilidad {
    @Autowired
    DisponibilidadRepository repDisponibilidad;

    @Autowired
    CupoService serCupo;

    @Autowired
    HorarioService serHorario;

    @Autowired
    EstacionService serEstacion;

    @Override
    public int guardarDisponibilidad(Disponibilidad disponibilidad) {
        repDisponibilidad.save(disponibilidad);
        return 0;
    }

    @Override
    public Disponibilidad consultarDisponibilidadMesDia(int mes, int dia, Cupo cupo) {
        Optional<Disponibilidad> respuesta = repDisponibilidad.findByMesAndDiaAndCupo(mes, dia, cupo);
        if(!respuesta.isPresent())
            return null;
        return respuesta.get();
    }

    @Override
    public Integer cuposDisponiblesEnDia(int dia, int mes) {
        Optional<Integer> respuesta = repDisponibilidad.cuposDisponiblesEnDia(dia, mes);
        if(!respuesta.isPresent())
            return null;
        return respuesta.get();
    }

    @Override
    public void actualizarCuposDia(int dia, int mes) {
        // Obtener la fecha con un mes y dia dado
        int anioActual = LocalDate.now().getYear();
        LocalDate fecha = LocalDate.of(anioActual, mes, dia);
        
        int valorDiaActual = DayOfWeek.from(fecha).getValue();

        List<Cupo> cupos = serCupo.obtenerTodos();
        if(valorDiaActual == 5)
            cupos = serCupo.obtenerTodosViernes();
        for(Cupo cupo: cupos){
            if(cupo.getCupoGrupo() == 0){
                this.guardarDisponibilidad(new Disponibilidad(0, mes, dia, cupo, cupo.getNum_cupos()));
            }
        }
    }

    @Override
    public List<Horario> horariosDisponiblesDiaMes(int dia, int mes) {
        if(dia == 0){
            return serHorario.obtenerTodos();
        }
        // Obtener la fecha con un mes y dia dado
        int anioActual = LocalDate.now().getYear();
        LocalDate fecha = LocalDate.of(anioActual, mes, dia);
        
        int valorDiaActual = DayOfWeek.from(fecha).getValue();
        
        List<Integer> idHorarios = new ArrayList<>();

        if(valorDiaActual != 5) 
            idHorarios = repDisponibilidad.idHorariosDisponiblesDiaAndMes(dia, mes);
        else
            idHorarios = repDisponibilidad.idHorariosDisponiblesDiaAndMesViernes(dia,mes);
        List<Horario> horarios = new ArrayList<>();

        if(idHorarios.isEmpty()){
            horarios = serHorario.obtenerTodos();
            if(valorDiaActual == 5)
                horarios.remove(horarios.size()-1);
            return horarios;
        }
        for(int id: idHorarios){
            Horario horario = serHorario.buscarPorId(id);
            Cupo cupo = serCupo.buscarPorId(serCupo.buscarIdPorCupoGrupoHorario((int)horario.getId()));
            if(cupo != null)
                horarios.add(serHorario.buscarPorId(cupo.getCupoGrupo()));
            horarios.add(horario);
        }
        return horarios;
    }

    @Override
    public List<Estacion> estacionesDisponiblesDiaMesHorario(int dia, int mes, int idHorario) {
        if(dia == 0){
            return serEstacion.obtenerTodas();
        }
        List<Integer> idEstaciones = new ArrayList<>();
        if(this.cuposDisponiblesEnDia(dia, mes) == null)
            idEstaciones = serCupo.idEstacionesPorHorario(idHorario);
        else         
            idEstaciones = repDisponibilidad.idEstacionesDisponiblesDiaAndMesAndHorario(dia, mes, idHorario);
        
            List<Estacion> estaciones = new ArrayList<>();
        
        for(int id: idEstaciones){
            Estacion estacion = serEstacion.buscarPorId(id);
            estaciones.add(estacion);
        }
        return estaciones;
    }

    @Override
    public List<Integer> diasSinDisponibilidadEnHorario(int mes, int idHorario) {
        return repDisponibilidad.diasSinDisponibilidadEnHorario(mes, idHorario);
    }

    @Override
    public List<Disponibilidad> obtenerTodasPorDiaMes(int dia, int mes) {
        return repDisponibilidad.findByDiaAndMes(dia, mes);
    }

    

    

    

}
