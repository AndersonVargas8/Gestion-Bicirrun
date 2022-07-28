package com.app.springapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.springapp.dto.TurnoDTO;
import com.app.springapp.entity.Turno;
import com.app.springapp.interfacesServicios.IServicioEstacion;
import com.app.springapp.interfacesServicios.IServicioEstudiante;
import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.repository.EstadoTurnoRepository;

public class Mapper {

    /**
     * Mapea un TurnoDTO a un Turno
     * @param dto
     * @return Turno con los datos contenidos enel DTO.
     * @throws IllegalArgumentException si faltan datos o son inválidos.
     */
    public static Turno mapTurno(TurnoDTO dto, IServicioEstacion serEstacion, IServicioEstudiante serEstudiante, IServicioHorario serHorario, EstadoTurnoRepository repEstadoTurno) {
        Turno turno = new Turno();

        if(!esFechaValida(dto.fecha)){
            throw new IllegalArgumentException("El formato de la fecha no es válido");
        }else{
            String[] fecha = dto.fecha.split("-");
            turno.setDia(Integer.parseInt(fecha[0]));
            turno.setMes(Integer.parseInt(fecha[1]));
            turno.setAnio(Integer.parseInt(fecha[2]));
        }

        turno.setObservaciones((dto.observaciones == null) ? "" : dto.observaciones);

        if(dto.idEstacion == 0){
            throw new IllegalArgumentException("No se indicó el id de la estación");
        }else{
            turno.setEstacion(serEstacion.buscarPorId(dto.idEstacion));
        }
        
        if(dto.idEstado == 0){
            turno.setEstado(repEstadoTurno.findById(1L).get());
        }else{
            turno.setEstado(repEstadoTurno.findById(new Long(dto.idEstado)).get());
        }

        if(dto.idEstudiante == 0){
            throw new IllegalArgumentException("No se indicó el id del estudiante");
        }else{
            turno.setEstudiante(serEstudiante.buscarPorId(new Long(dto.idEstudiante)));
        }

        if(dto.idHorario == 0){
            throw new IllegalArgumentException("No se indicó el id del horario");
        }else{
            turno.setHorario(serHorario.buscarPorId(dto.idHorario));
        }

        return turno;
    }

    private static boolean esFechaValida(String entrada){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try{
            format.parse(entrada);
            return true;
        }catch(ParseException e){
            return false;
        }
    }
}
