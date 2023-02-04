package com.app.springapp.dto;

import java.math.BigInteger;

public class ReporteTurnosEstudiante {
    private Long documento;
    private String nombres;
    private String apellidos;
    private Long turnosProgramados;
    private Long horasProgramadas;
    private Long turnosCumplidos;
    private Long horasCumplidas;
    private Long turnosIncumplidos;
    private Long horasIncumplidas;

    public ReporteTurnosEstudiante(Long documento, String nombres, String apellidos, Long turnosProgramados,
            Long horasProgramadas, Long turnosCumplidos, Long horasCumplidas, Long turnosIncumplidos,
            Long horasIncumplidas) {
        this.documento = documento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.turnosProgramados = turnosProgramados;
        this.horasProgramadas = horasProgramadas;
        this.turnosCumplidos = turnosCumplidos;
        this.horasCumplidas = horasCumplidas;
        this.turnosIncumplidos = turnosIncumplidos;
        this.horasIncumplidas = horasIncumplidas;
    }

    public ReporteTurnosEstudiante(Long documento, String nombres, String apellidos, Long turnosProgramados,
            Long horasProgramadas) {
        this.documento = documento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.turnosProgramados = turnosProgramados;
        this.horasProgramadas = horasProgramadas;
    }

    public ReporteTurnosEstudiante(BigInteger documento, String nombres, String apellidos, BigInteger turnosProgramados,
            BigInteger horasProgramadas) {
        this.documento = documento.longValue();
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.turnosProgramados = turnosProgramados.longValue();
        this.horasProgramadas = horasProgramadas.longValue();
    }

    public enum TipoTurno{CUMPLIDO, INCUMPLIDO}
    public ReporteTurnosEstudiante(TipoTurno tipoTurno, BigInteger turnos, BigInteger horas) {
        if(tipoTurno.equals(TipoTurno.CUMPLIDO)){
            this.turnosCumplidos = turnos.longValue();
            this.horasCumplidas = horas.longValue();
        }
        if(tipoTurno.equals(TipoTurno.INCUMPLIDO)){
            this.turnosIncumplidos = turnos.longValue();
            this.horasIncumplidas = horas.longValue();
        }
    }

    public Long getDocumento() {
        return documento;
    }

    public void setDocumento(Long documento) {
        this.documento = documento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Long getTurnosProgramados() {
        return turnosProgramados;
    }

    public void setTurnosProgramados(Long turnosProgramados) {
        this.turnosProgramados = turnosProgramados;
    }

    public Long getHorasProgramadas() {
        return horasProgramadas;
    }

    public void setHorasProgramadas(Long horasProgramadas) {
        this.horasProgramadas = horasProgramadas;
    }

    public Long getTurnosCumplidos() {
        return turnosCumplidos;
    }

    public void setTurnosCumplidos(Long turnosCumplidos) {
        this.turnosCumplidos = turnosCumplidos;
    }

    public Long getHorasCumplidas() {
        return horasCumplidas;
    }

    public void setHorasCumplidas(Long horasCumplidas) {
        this.horasCumplidas = horasCumplidas;
    }

    public Long getTurnosIncumplidos() {
        return turnosIncumplidos;
    }

    public void setTurnosIncumplidos(Long turnosIncumplidos) {
        this.turnosIncumplidos = turnosIncumplidos;
    }

    public Long getHorasIncumplidas() {
        return horasIncumplidas;
    }

    public void setHorasIncumplidas(Long horasIncumplidas) {
        this.horasIncumplidas = horasIncumplidas;
    }

}
