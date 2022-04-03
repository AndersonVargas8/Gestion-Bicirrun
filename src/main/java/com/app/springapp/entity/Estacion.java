package com.app.springapp.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Estacion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Column
    @NotBlank
    private String nombre;

    @Column
    @NotBlank
    private int cupos;


    public Estacion() {
    }

    public Estacion(long id, String nombre, int cupos) {
        this.id = id;
        this.nombre = nombre;
        this.cupos = cupos;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCupos() {
        return this.cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Estacion)) {
            return false;
        }
        Estacion estacion = (Estacion) o;
        return id == estacion.id && Objects.equals(nombre, estacion.nombre) && cupos == estacion.cupos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, cupos);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", cupos='" + getCupos() + "'" +
            "}";
    }

}

