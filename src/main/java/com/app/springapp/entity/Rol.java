package com.app.springapp.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Esta clase define objetos para manejar la persistencia de roles dentro de la aplicación
 */
@Entity
public class Rol implements Serializable {
    private static final long serialVersionUID = -2969524610059270447L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "rol_nombre")
    private String nombre;

    @Column(name = "rol_descripcion")
    private String descripcion;

    
    /** 
     * @return long
     */
    public long getId() {
        return this.id;
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
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                " id = '" + getId() + "'" +
                ", name = '" + getNombre() + "'" +
                ", description = '" + getDescripcion() + "'" +
                "}";
    }

    
    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Rol)) {
            return false;
        }
        Rol rol = (Rol) o;
        return id == rol.id && Objects.equals(nombre, rol.nombre) && Objects.equals(descripcion, rol.descripcion);
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, descripcion);
    }

}
