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
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Column 
	@NotBlank
	private String nombres;

    @Column 
	@NotBlank
	private String apellidos;

    @Column 
	@NotBlank
	private String documento;

    @ManyToOne(optional = false, cascade = CascadeType.ALL,  fetch= FetchType.EAGER)
    private Carrera carrera;

    public Estudiante() {
    }

    public Estudiante(long id, String nombres, String apellidos, String documento, Carrera carrera) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documento = documento;
        this.carrera = carrera;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombres() {
        return this.nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDocumento() {
        return this.documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Carrera getCarrera() {
        return this.carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Estudiante)) {
            return false;
        }
        Estudiante estudiante = (Estudiante) o;
        return id == estudiante.id && Objects.equals(nombres, estudiante.nombres) && Objects.equals(apellidos, estudiante.apellidos) && Objects.equals(documento, estudiante.documento) && Objects.equals(carrera, estudiante.carrera);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombres, apellidos, documento, carrera);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nombres='" + getNombres() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            ", documento='" + getDocumento() + "'" +
            ", carrera='" + getCarrera() + "'" +
            "}";
    }

}
