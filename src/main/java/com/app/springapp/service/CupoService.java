package com.app.springapp.service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.app.springapp.dto.Calendario;
import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;
import com.app.springapp.entity.Horario.Dia;
import com.app.springapp.interfacesServicios.IServicioCupo;
import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.repository.CupoRepository;import com.app.springapp.repository.HorarioRepository;
import com.app.springapp.repository.HorarioRepository.IHorariosDiasNoDisponibles;
@Service
public class CupoService implements IServicioCupo{
    @Autowired
    CupoRepository repCupo;

    @Lazy
    @Autowired
    IServicioHorario serHorario;

    @Autowired
    HorarioRepository repHorario;

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
    /**
     * Retorna la cantidad de cupos que hay en cada día de la semana (Lunes a Viernes)
     * @return HashMap - clave: nombre del Dia, valor: cantidad de cupos
     */
    public HashMap<Integer, Integer> cantidadCuposAlDia(){
        int totalCupos = (int)repCupo.sumTotalCupos();
        Map<Long, Integer> cuposHorarios = repCupo.sumCuposGroupByHorario();
        
        HashMap<Integer,Integer> cupos = new HashMap<>();

        List<IHorariosDiasNoDisponibles> horariosDiasNoDisp = repHorario.findHorariosDiasNoDisponibles();
        
        
        for(int i = 1; i <= 5; i++){
            //Se guarda la suma de cupos total de todos los horarios para el día correspondiente
            cupos.put(i, totalCupos);
        }

        for(IHorariosDiasNoDisponibles horarioNoDisp: horariosDiasNoDisp){
            int dia = Dia.valueOf(horarioNoDisp.getDia()).getValue();
            int numCupos = cuposHorarios.get(horarioNoDisp.getHorarioId());
            cupos.put(dia, cupos.get(dia) - numCupos);
        }

        return cupos;
    }

    @Override
    public int cantidadCuposPorFecha(int dia, int mes, int anio){
        LocalDate fecha = LocalDate.of(anio,mes,dia);

        int valorDiaSemana = fecha.get(WeekFields.ISO.dayOfWeek());

        return cantidadCuposAlDia().get(valorDiaSemana);
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
    @Override
    public int cantidadCuposAbsolutos(Horario horario) {
        int numeroCupos = cantidadCuposPorHorario(horario);

        if(numeroCupos == 0){
            List<Cupo> cupos = buscarPorHorario(horario);
            Set<Horario> horariosIndependientes = new HashSet<>();
            for(Cupo cupo: cupos){
                if(cupo.tieneCupoCompartido()){
                    horariosIndependientes.add(cupo.getCupoCompartido().getHorario());
                }
            }

            for(Horario horarioIt: horariosIndependientes){
                numeroCupos += cantidadCuposPorHorario(horarioIt);
            }
        }

        return numeroCupos;
    }

}

