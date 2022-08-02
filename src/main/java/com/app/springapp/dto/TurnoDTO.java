package com.app.springapp.dto;

import java.time.LocalDate;

public class TurnoDTO implements Comparable<TurnoDTO>{
    public int id;
    public String fecha;
    public String observaciones;
    public int idEstacion;
    public String estacion;
    public int idEstado;
    public String estado;
    public int idEstudiante;
    public String estudiante;
    public String nombreCortoEstudiante;
    public int idHorario;
    public String horario;

    @Override
    public int compareTo(TurnoDTO otro) {
        String[] fechaSeparada = fecha.split("-");
        LocalDate fechaThis = LocalDate.of(Integer.parseInt(fechaSeparada[2]),Integer.parseInt(fechaSeparada[1]),Integer.parseInt(fechaSeparada[0]));
        fechaSeparada = otro.fecha.split("-");
        LocalDate fechaOtro = LocalDate.of(Integer.parseInt(fechaSeparada[2]),Integer.parseInt(fechaSeparada[1]),Integer.parseInt(fechaSeparada[0]));
        
        int compare = fechaThis.compareTo(fechaOtro);

        if(compare == 0){
            if(idHorario < otro.idHorario)
                return -1;
            if(idHorario > otro.idHorario)
                return 1;
        }
        return 0;
    }

    

}
