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
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Column
    @NotBlank
    private String descripcion;

    @Column
    @NotBlank
    private int valor_horas;

    public Horario() {
    }

    public Horario(long id, String descripcion, int valor_horas) {
        this.id = id;
        this.descripcion = descripcion;
        this.valor_horas = valor_horas;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getValor_horas() {
        return this.valor_horas;
    }

    public void setValor_horas(int valor_horas) {
        this.valor_horas = valor_horas;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion, valor_horas);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", valor_horas='" + getValor_horas() + "'" +
            "}";
    }
  
}
