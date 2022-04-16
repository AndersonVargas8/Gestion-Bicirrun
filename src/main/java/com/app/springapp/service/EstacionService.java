package com.app.springapp.service;

import java.util.List;

import com.app.springapp.entity.Estacion;
import com.app.springapp.interfacesServicios.IServicioEstacion;
import com.app.springapp.repository.EstacionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstacionService implements IServicioEstacion{
    @Autowired
    EstacionRepository repEstacion;
    
    @Override
    public List<Estacion> obtenerTodas() {
        return (List<Estacion>)repEstacion.findAllByOrderByIdAsc();
    }

    @Override
    public Estacion buscarPorId(int id) {
        return repEstacion.findById(new Long(id)).get();
    }
    
}
