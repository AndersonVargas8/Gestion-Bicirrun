package com.app.springapp.service;

import java.util.List;
import java.util.Optional;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;
import com.app.springapp.interfacesServicios.IServicioCupo;
import com.app.springapp.repository.CupoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CupoService implements IServicioCupo{
    @Autowired
    CupoRepository repCupo;

    @Override
    public List<Cupo> obtenerTodos() {  
        return (List<Cupo>)repCupo.findAll();
    }
    @Override
    public List<Cupo> obtenerTodosViernes() {  
        return (List<Cupo>)repCupo.findAllViernes();
    }

    @Override
    public Cupo buscarPorHorario(Horario horario) {
        return repCupo.findByHorario(horario);
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

    

}

