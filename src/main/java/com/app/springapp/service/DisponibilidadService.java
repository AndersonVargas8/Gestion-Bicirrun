package com.app.springapp.service;

import java.util.List;
import java.util.Optional;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Disponibilidad;
import com.app.springapp.interfacesServicios.IServicioDisponibilidad;
import com.app.springapp.repository.CupoRepository;
import com.app.springapp.repository.DisponibilidadRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisponibilidadService implements IServicioDisponibilidad {
    @Autowired
    DisponibilidadRepository repDisponibilidad;

    @Autowired
    CupoRepository repCupo;

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
        List<Cupo> cupos = (List<Cupo>)repCupo.findAll();
        
        for(Cupo cupo: cupos){
            if(cupo.getCupoGrupo() == 0){
                this.guardarDisponibilidad(new Disponibilidad(0, mes, dia, cupo, cupo.getNum_cupos()));
            }
        }
    }

    

}
