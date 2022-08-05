package com.app.springapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;
import com.app.springapp.interfacesServicios.IServicioCupo;
import com.app.springapp.interfacesServicios.IServicioEstacion;
import com.app.springapp.interfacesServicios.IServicioTurno;
import com.app.springapp.repository.EstacionRepository;

@Lazy
@Service
public class EstacionService implements IServicioEstacion {
    @Autowired
    EstacionRepository repEstacion;

    @Autowired
    IServicioCupo serCupo;

    @Autowired
    IServicioTurno serTurno;

    @Override
    public List<Estacion> obtenerTodas() {
        return (List<Estacion>) repEstacion.findAllByOrderByIdAsc();
    }

    @Override
    public Estacion buscarPorId(int id) {
        return repEstacion.findById(new Long(id)).get();
    }

    @Override
    public List<Estacion> obtenerDisponiblesPorFechaYHorario(LocalDate fecha, Horario horario) {
        List<Estacion> listaEstaciones = obtenerTodas();
        List<Estacion> estacionesDisponibles = new ArrayList<>();
        for (Estacion estacion : listaEstaciones) {
            Cupo cupo = serCupo.buscarPorEstacionYHorario(estacion, horario);
            if(cupo == null){
                continue;
            }
            int numCupos = serCupo.obtenerNumeroCupos(cupo);
            int numTurnos = serTurno.sumaTurnosPorFechaYHorarioYEstacion(fecha, horario, estacion);

            if(cupo.tieneCupoCompartido()){
                numTurnos += serTurno.sumaTurnosPorFechaYHorarioYEstacion(fecha, cupo.getCupoCompartido().getHorario(), estacion);
            }
            if (numTurnos < numCupos) {
                estacionesDisponibles.add(estacion);
            }
        }
        return estacionesDisponibles;
    }

}
