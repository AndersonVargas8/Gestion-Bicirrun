package com.app.springapp.service;

import java.util.List;
import java.util.Optional;

import com.app.springapp.entity.Cupo;
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
    public Cupo buscarPorHorario(Horario horario) {
        return repCupo.findByHorario(horario);
    }

    @Override
    public Cupo buscarPorCupoGrupo(int cupoGrupoId) {
        Optional<Cupo> respuesta = repCupo.findByCupoGrupo(cupoGrupoId);
        if(respuesta.isPresent())
            return respuesta.get();
        
        return null;
    }

    @Override
    public int buscarIdPorCupoGrupoHoraroi(int idHorario) {
        Optional<Integer> respuesta = repCupo.findIdByCupoGrupoHorario(idHorario);
        if(respuesta.isPresent())
            return respuesta.get();
        return -1;
    }

    @Override
    public Cupo buscarPorId(int id) {
        return repCupo.findById(new Long(id));
    }

    

}

