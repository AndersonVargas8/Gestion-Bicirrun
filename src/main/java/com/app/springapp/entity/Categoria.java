package com.app.springapp.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;


/**
 * Esta clase permite manejar la persistencia de las categorías a las que pertenecen los estudiantes
 */
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column 
	@NotBlank
	private String descripcion;

    @ManyToMany(mappedBy = "categorias")
    private List<Estudiante> estudiantes;

    public Categoria() {}

    public Categoria(long id, String descripcion, List<Estudiante> estudiantes) {
        this.id = id;
        this.descripcion = descripcion;
        this.estudiantes = estudiantes;
    }
    

    public Categoria(long id, String descripcion) {
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
     * @return Lista de estudiantes que pertenecen a esta categoría
     */
    public List<Estudiante> getEstudiantes() {
        return this.estudiantes;
    }

    
    /** 
     * @param estudiantes
     */
    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }
    
    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Categoria)) {
            return false;
        }
        Categoria categoria = (Categoria) o;
        return id == categoria.id && Objects.equals(descripcion, categoria.descripcion);
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
