package com.app.springapp.service;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
        List<Cupo> cupos = buscarPorHorario(horario);

        int suma = 0;

        for(Cupo cupo: cupos){
            suma += cupo.getNum_cupos();
        }

        return suma;
    }

    /*@Override
    public Cupo buscarPorCupoGrupo(int cupoGrupoId) {
        Optional<Cupo> respuesta = repCupo.findByCupoGrupo(cupoGrupoId);
        if(respuesta.isPresent())
            return respuesta.get();
        
        return null;
    }*/

    /*@Override
    public int buscarIdPorCupoGrupoHorario(int idHorario) {
        Optional<Integer> respuesta = repCupo.findIdByCupoGrupoHorario(idHorario);
        if(respuesta.isPresent())
            return respuesta.get();
        return -1;
    }*/

    @Override
    public Cupo buscarPorId(int id) {
        return repCupo.findById(new Long(id));
    }

    /*@Override
    public Integer cantidadCupos() {
        return repCupo.sumNumCupos();
    }*/

    /*@Override
    public Integer cantidadCuposViernes() {
        return repCupo.sumNumCuposViernes();
    }*/

    @Override
    public Cupo buscarPorEstacionYHorario(Estacion estacion, Horario horario) {
        return repCupo.findByEstacionAndHorario(estacion, horario).get();
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
        if(!cupo.getCupos_independientes().isEmpty()){
            numeroCupos = cupo.getCupos_independientes().get(0).getNum_cupos();
        }
        return numeroCupos;
    }

}

