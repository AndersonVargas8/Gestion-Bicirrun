package com.app.springapp.service;

import java.util.Collections;
import java.util.List;

import com.app.springapp.entity.Estudiante;
import com.app.springapp.entity.Turno;
import com.app.springapp.interfacesServicios.IServicioTurno;
import com.app.springapp.repository.TurnoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnoService implements IServicioTurno{
    @Autowired
    TurnoRepository repTurno;

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
}
