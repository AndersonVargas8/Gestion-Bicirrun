package com.app.springapp.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;


/**
 * Esta clase permite manejar la persistenia de los horarios para los turnos 
 */
@Entity
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @NotBlank
    private String descripcion;

    @Column
    @NotBlank
    private int valor_horas;

    public enum Dia{
        lunes, martes, miércoles, jueves, viernes;

        public int getValue(){
            return ordinal() + 1;
        }
    }

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<Dia> diasNoDisponibles = new HashSet<>();

    public Horario() {
    }

    public Horario(long id, String descripcion, int valor_horas) {
        this.id = id;
        this.descripcion = descripcion;
        this.valor_horas = valor_horas;
    }

    public Horario(long id, String descripcion, int valor_horas, Set<Dia> diasNoDisponibles) {
        this.id = id;
        this.descripcion = descripcion;
        this.valor_horas = valor_horas;
        this.diasNoDisponibles = diasNoDisponibles;
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
     * @return int
     */
    public int getValor_horas() {
        return this.valor_horas;
    }

    
    /** 
     * @param valor_horas
     */
    public void setValor_horas(int valor_horas) {
        this.valor_horas = valor_horas;
    }


    
    /** 
     * @return Set<String>
     */
    public Set<Dia> getDiasNoDisponibles() {
        return this.diasNoDisponibles;
    }

    
    /** 
     * @param diasNoDisponibles
     */
    public void setDiasNoDisponibles(Set<Dia> diasNoDisponibles) {
        this.diasNoDisponibles = diasNoDisponibles;
    }
    
    /**
     * 
     * @param dia
     * @return True si el dia especificado no está disponible. False en otro caso.
     */
    public boolean diaNoDisponible(String dia){
        dia = dia.toLowerCase();

        return this.diasNoDisponibles.contains(Dia.valueOf(dia));
    }

    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Horario)) {
            return false;
        }
        Horario horario = (Horario) o;
        return id == horario.id && Objects.equals(descripcion, horario.descripcion) && valor_horas == horario.valor_horas;
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion, valor_horas);
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", valor_horas='" + getValor_horas() + "'" +
            "}";
    }

}
