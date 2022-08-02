package com.app.springapp.service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.app.springapp.dto.Calendario;
import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;
import com.app.springapp.interfacesServicios.IServicioCupo;
import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.repository.CupoRepository;

@Service
public class CupoService implements IServicioCupo{
    @Autowired
    CupoRepository repCupo;

    @Lazy
    @Autowired
    IServicioHorario serHorario;

    @Override
    public List<Cupo> obtenerTodos() {  
        return (List<Cupo>)repCupo.findAll();
    }
    @Override
    public List<Cupo> obtenerTodosViernes() {  
        return (List<Cupo>)repCupo.findAllViernes();
    }

    @Override
    public List<Cupo> buscarPorHorario(Horario horario) {
        return repCupo.findByHorario(horario);
    }

    @Override
    public int cantidadCuposPorHorario(Horario horario){
        long numeroCupos = repCupo.sumCuposByHorario(horario);
        return (int)numeroCupos;
    }

    @Override
    public Cupo buscarPorId(int id) {
        return repCupo.findById(new Long(id));
    }

    @Override
    public Cupo buscarPorEstacionYHorario(Estacion estacion, Horario horario) {
        Cupo cupo = null;
        try{
            cupo = repCupo.findByEstacionAndHorario(estacion, horario).get();
        }catch(NoSuchElementException e){
            return null;
        }
        return cupo;
    }

    @Override
    public List<Integer> idEstacionesPorHorario(int idHorario){
        return repCupo.idEstacionesPorHorario(new Long(idHorario));
    }

    /**************************** */
    /********NUEVO SERVICIO****** */
    /**************************** */

    @Override
    public HashMap<String, Integer> cantidadCuposAlDia(){
        HashMap<String,Integer> cupos = new HashMap<>();

        List<Horario> horarios = serHorario.obtenerTodos();
        
        for(int i = 1; i <= 5; i++){
            int sumaCupos = 0;

            //Se obtiene el nombre del día: (1)Lunes, (2)Martes,...
            String dia = Calendario.convertirNumeroADia(i);

            //Por cada horario, se verifica que esté disponible para el día y se suma su cantidad de cupos total
            for(Horario horario: horarios){
                if(horario.diaNoDisponible(dia))
                    continue;
                
                sumaCupos += cantidadCuposPorHorario(horario);
            }

            //Se guarda la suma de cupos total de todos los horarios para el día correspondiente
            cupos.put(dia, sumaCupos);
        }

        return cupos;
    }

    @Override
    public int cantidadCuposPorFecha(int dia, int mes, int anio){
        LocalDate fecha = LocalDate.of(anio,mes,dia);

        int valorDiaSemana = fecha.get(WeekFields.ISO.dayOfWeek());
        String nombreDia = Calendario.convertirNumeroADia(valorDiaSemana);

        return cantidadCuposAlDia().get(nombreDia);
    }

    @Override
    public int obtenerNumeroCupos(Cupo cupo){
        int numeroCupos = cupo.getNum_cupos();
        if(cupo.tieneCupoCompartido()){
            numeroCupos += cupo.getCupoCompartido().getNum_cupos();
        }
        return numeroCupos;
    }

    @Override
    public int cantidadCupos(LocalDate fecha, Horario horario, Estacion estacion) {
        int valorDiaSemana = fecha.get(WeekFields.ISO.dayOfWeek());
        String nombreDia = Calendario.convertirNumeroADia(valorDiaSemana).toLowerCase();
        if(horario.diaNoDisponible(nombreDia)){
            return 0;
        }
        Cupo cupo = buscarPorEstacionYHorario(estacion, horario);
        if(cupo ==  null){
            return 0;
        }
        int numCupos = obtenerNumeroCupos(cupo);

        return numCupos;
    }

}

