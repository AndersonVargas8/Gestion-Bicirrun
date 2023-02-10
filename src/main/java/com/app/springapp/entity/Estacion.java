package com.app.springapp.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * Esta clase permite manejar la persistencia de las estaciones de bicirrun
 */
@Entity
public class Estacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank
    private String nombre;

    @Column
    private Boolean isHabilitada;

    public Estacion() {
    }

    public Estacion(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.isHabilitada = true;
    }

    
    /** 
     * @return long
     */
    public long getId() {
        return this.id;
    }

    
    /** 
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    
    /** 
     * @return String
     */
    public String getNombre() {
        return this.nombre;
    }

    
    /** 
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Estacion)) {
            return false;
        }
        Estacion estacion = (Estacion) o;
        return id == estacion.id && Objects.equals(nombre, estacion.nombre);
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            "}";
    }

    public Boolean isHabilitada() {
        return isHabilitada;
    }

    private void setIsHabilitada(Boolean isHabilitada) {
        this.isHabilitada = isHabilitada;
    }

    public void habilitar(){
        this.setIsHabilitada(true);
    }

    public void inhabilitar(){
        this.setIsHabilitada(false);
    }

}

