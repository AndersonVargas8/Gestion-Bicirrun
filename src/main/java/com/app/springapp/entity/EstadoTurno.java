package com.app.springapp.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * Esta clase permite  manejar la persistencia de los estados de los turnos creados en la aplicación
 */
@Entity
public class EstadoTurno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank
    private String descripcion;


    public EstadoTurno() {
    }

    public EstadoTurno(long id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
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
    public String getDescripcion() {
        return this.descripcion;
    }

    
    /** 
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EstadoTurno)) {
            return false;
        }
        EstadoTurno estadoTurno = (EstadoTurno) o;
        return id == estadoTurno.id && Objects.equals(descripcion, estadoTurno.descripcion);
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion);
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }


}
