package com.app.springapp.service;

import java.util.List;

import com.app.springapp.entity.Horario;
import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.repository.HorarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorarioService implements IServicioHorario{
    @Autowired
    HorarioRepository repHorario;

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

    
}
