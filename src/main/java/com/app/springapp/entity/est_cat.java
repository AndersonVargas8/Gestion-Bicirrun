package com.app.springapp.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class est_cat {
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "est_id")
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cat_id")
    private Categoria categoria;

    private int horas;


    public est_cat() {
    }

    public est_cat(long id, Estudiante estudiante, Categoria categoria, int horas) {
        this.id = id;
        this.estudiante = estudiante;
        this.categoria = categoria;
        this.horas = horas;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return this.estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getHoras() {
        return this.horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public est_cat id(long id) {
        setId(id);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof est_cat)) {
            return false;
        }
        est_cat est_cat = (est_cat) o;
        return id == est_cat.id && Objects.equals(estudiante, est_cat.estudiante) && Objects.equals(categoria, est_cat.categoria) && horas == est_cat.horas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, estudiante, categoria, horas);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", estudiante='" + getEstudiante() + "'" +
            ", categoria='" + getCategoria() + "'" +
            ", horas='" + getHoras() + "'" +
            "}";
    }
    
}
