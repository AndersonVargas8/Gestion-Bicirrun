package com.app.springapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.app.springapp.Exception.CustomeFieldValidationException;
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
     * @throws CustomeFieldValidationException
     * @throws IllegalArgumentException si faltan datos o son inválidos.
     */
    public static Turno mapToTurno(TurnoDTO dto, IServicioEstacion serEstacion, IServicioEstudiante serEstudiante, IServicioHorario serHorario, EstadoTurnoRepository repEstadoTurno) throws CustomeFieldValidationException {
        Turno turno = new Turno();

        if(!esFechaValida(dto.fecha)){
            throw new CustomeFieldValidationException("El formato de la fecha no es válido");
        }else{
            String[] fecha = dto.fecha.split("-");
            turno.setDia(Integer.parseInt(fecha[0]));
            turno.setMes(Integer.parseInt(fecha[1]));
            turno.setAnio(Integer.parseInt(fecha[2]));
        }

        if(dto.idEstado == 0){
            turno.setEstado(repEstadoTurno.findById(1L).get());
        }else{
            turno.setEstado(repEstadoTurno.findById(new Long(dto.idEstado)).get());
        }
        
        turno.setObservaciones((dto.observaciones == null) ? "" : dto.observaciones);
        
        if(dto.idEstudiante == 0){
            throw new CustomeFieldValidationException("No se indicó el estudiante");
        }else{
            turno.setEstudiante(serEstudiante.buscarPorId(new Long(dto.idEstudiante)));
        }

        if(dto.idHorario == 0){
            throw new CustomeFieldValidationException("No se indicó el horario");
        }else{
            turno.setHorario(serHorario.buscarPorId(dto.idHorario));
        }
        
        if(dto.idEstacion == 0){
            throw new CustomeFieldValidationException("No se indicó la estación");
        }else{
            turno.setEstacion(serEstacion.buscarPorId(dto.idEstacion));
        }

        return turno;
    }

    public static TurnoDTO mapToTurnoDTO(Turno turno){
        TurnoDTO turnoDTO = new TurnoDTO();
        turnoDTO.fecha = String.valueOf(turno.getDia()) + "-" + String.valueOf(turno.getMes()) + "-" + String.valueOf(turno.getAnio());
        turnoDTO.observaciones = turno.getObservaciones();
        turnoDTO.estacion = turno.getEstacion().getNombre();
        turnoDTO.estado = turno.getEstado().getDescripcion();
        turnoDTO.estudiante = turno.getEstudiante().getNombres() + " " + turno.getEstudiante().getApellidos();

        String nombre = turno.getEstudiante().getNombres().split(" ")[0];
        String apellido = turno.getEstudiante().getApellidos().split(" ")[0];

        turnoDTO.nombreCortoEstudiante = nombre + " " + apellido;
        turnoDTO.horario = turno.getHorario().getDescripcion();

        return turnoDTO;
    }

    public static TurnoDTO mapToTurnoDTOFull(Turno turno){
        TurnoDTO turnoDTO = new TurnoDTO();
        turnoDTO.id = (int)turno.getId();
        turnoDTO.fecha = String.valueOf(turno.getDia()) + "-" + String.valueOf(turno.getMes()) + "-" + String.valueOf(turno.getAnio());
        turnoDTO.observaciones = turno.getObservaciones();
        turnoDTO.idEstacion = (int)turno.getEstacion().getId();
        turnoDTO.estacion = turno.getEstacion().getNombre();
        turnoDTO.idEstado = (int)turno.getEstado().getId();
        turnoDTO.estado = turno.getEstado().getDescripcion();
        turnoDTO.idEstudiante = (int)turno.getEstudiante().getId();
        turnoDTO.estudiante = turno.getEstudiante().getNombres() + " " + turno.getEstudiante().getApellidos();

        String nombre = turno.getEstudiante().getNombres().split(" ")[0];
        String apellido = turno.getEstudiante().getApellidos().split(" ")[0];

        turnoDTO.nombreCortoEstudiante = nombre + " " + apellido;
        turnoDTO.idHorario = (int)turno.getHorario().getId();
        turnoDTO.horario = turno.getHorario().getDescripcion();

        return turnoDTO;
    }

    public static boolean esFechaValida(String entrada){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try{
            format.parse(entrada);
            return true;
        }catch(ParseException e){
            return false;
        }
    }
}
