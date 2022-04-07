package com.app.springapp.entity;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Column
    @NotNull
    private int dia;

    @Column
    @NotNull
    private int mes;

    @Column
    private String observaciones;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private EstadoTurno estado;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Estacion estacion;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Estudiante estudiante;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Horario horario;

    public Turno() {
    }

    public Turno(long id, int dia, int mes, String observaciones, EstadoTurno estado, Estacion estacion,
            Estudiante estudiante, Horario horario) {
        this.id = id;
        this.dia = dia;
        this.mes = mes;
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
        return id == turno.id && dia == turno.dia && mes == turno.mes
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
                ", observaciones='" + getObservaciones() + "'" +
                ", estado='" + getEstado() + "'" +
                ", estacion='" + getEstacion() + "'" +
                ", estudiante='" + getEstudiante() + "'" +
                ", horario='" + getHorario() + "'" +
                "}";
    }

}
