package com.app.springapp.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

/**
 * Esta clase permite manejar la persistencia de los turnos creados en la aplicación
 */
@Entity
public class Turno implements Comparable<Turno>, Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Column
    private int dia;

    @Column
    private int mes;

    @Column
    private int anio;

    @Column
    private String observaciones;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private EstadoTurno estado;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Estacion estacion;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Estudiante estudiante;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Horario horario;

    public Turno() {
    }

    public Turno(long id, int dia, int mes, int anio, String observaciones, EstadoTurno estado, Estacion estacion,
            Estudiante estudiante, Horario horario) {
        this.id = id;
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
        this.observaciones = observaciones;
        this.estado = estado;
        this.estacion = estacion;
        this.estudiante = estudiante;
        this.horario = horario;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDia() {
        return this.dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return this.mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return this.anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoTurno getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }

    public Estacion getEstacion() {
        return this.estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }

    public Estudiante getEstudiante() {
        return this.estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Horario getHorario() {
        return this.horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Turno)) {
            return false;
        }
        Turno turno = (Turno) o;
        return id == turno.id && dia == turno.dia && mes == turno.mes && anio == turno.anio
                && Objects.equals(observaciones, turno.observaciones) && Objects.equals(estado, turno.estado)
                && Objects.equals(estacion, turno.estacion) && Objects.equals(estudiante, turno.estudiante)
                && Objects.equals(horario, turno.horario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dia, mes, observaciones, estado, estacion, estudiante, horario);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", dia='" + getDia() + "'" +
                ", mes='" + getMes() + "'" +
                ", anio='" + getAnio() + "'" +
                ", observaciones='" + getObservaciones() + "'" +
                ", estado='" + getEstado() + "'" +
                ", estacion='" + getEstacion() + "'" +
                ", estudiante='" + getEstudiante() + "'" +
                ", horario='" + getHorario() + "'" +
                "}";
    }

    @Override
    public int compareTo(Turno turno) {
        LocalDate fechaThis = LocalDate.of(anio,mes,dia);
        LocalDate fechaOtro = LocalDate.of(turno.anio, turno.mes, turno.dia);

        int compare = fechaThis.compareTo(fechaOtro);

        if(compare == 0){
            if(horario.getId() < turno.getHorario().getId()){
                return -1;
            }
            if(horario.getId() > turno.getHorario().getId()){
                return 1;
            }
        }

        return compare;
    }

    @Override
    public Turno clone(){
        try {
            return (Turno)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
