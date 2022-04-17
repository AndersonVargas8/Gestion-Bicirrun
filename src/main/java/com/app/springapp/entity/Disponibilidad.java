
package com.app.springapp.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="disponibilidad")
public class Disponibilidad {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Column
    private int mes;

    @Column
    private int dia;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cupo_id", nullable = true)
    private Cupo cupo;

    @Column
    private int num_disponibles;


    public Disponibilidad() {
    }

    public Disponibilidad(long id, int mes, int dia, Cupo cupo, int num_disponibles) {
        this.id = id;
        this.mes = mes;
        this.dia = dia;
        this.cupo = cupo;
        this.num_disponibles = num_disponibles;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMes() {
        return this.mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return this.dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public Cupo getCupo() {
        return this.cupo;
    }

    public void setCupo(Cupo cupo) {
        this.cupo = cupo;
    }

    public int getNum_disponibles() {
        return this.num_disponibles;
    }

    public void setNum_disponibles(int num_disponibles) {
        this.num_disponibles = num_disponibles;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Disponibilidad)) {
            return false;
        }
        Disponibilidad disponibilidad = (Disponibilidad) o;
        return id == disponibilidad.id && mes == disponibilidad.mes && dia == disponibilidad.dia && Objects.equals(cupo, disponibilidad.cupo) && num_disponibles == disponibilidad.num_disponibles;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mes, dia, cupo, num_disponibles);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", mes='" + getMes() + "'" +
            ", dia='" + getDia() + "'" +
            ", cupo='" + getCupo() + "'" +
            ", num_disponibles='" + getNum_disponibles() + "'" +
            "}";
    }
    
}
