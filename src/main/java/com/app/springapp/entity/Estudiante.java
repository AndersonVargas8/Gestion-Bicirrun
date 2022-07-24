package com.app.springapp.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

/**
 * Esta clase permite manejar la persistencia de los estudiantes
 */
@Entity
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column 
	@NotBlank
	private String nombres;

    @Column 
	@NotBlank
	private String apellidos;

    @Column (unique=true)
	@NotBlank
	private String documento;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST,CascadeType.REFRESH},  fetch= FetchType.EAGER)
    private Carrera carrera;

    @Column
    private String telefono;

    @ManyToMany
    @JoinTable(name = "estudiantes_categorias",
    joinColumns = @JoinColumn(name = "id_estudiante"),
    inverseJoinColumns = @JoinColumn(name = "id_categoria"))
    private List<Categoria> categorias;

    public Estudiante() {
    }

    public Estudiante(long id, String nombres, String apellidos, String documento, Carrera carrera, String telefono) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documento = documento;
        this.carrera = carrera;
        this.telefono = telefono;
    }


    public Estudiante(long id, String nombres, String apellidos, String documento, Carrera carrera, String telefono, List<Categoria> categorias) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documento = documento;
        this.carrera = carrera;
        this.telefono = telefono;
        this.categorias = categorias;
    }
    
    /** 
     * @return String
     */
    public String getTelefono() {
        return telefono;
    }

    
    /** 
     * @param telefono
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
    public String getNombres() {
        return this.nombres;
    }

    
    /** 
     * @param nombres
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    
    /** 
     * @return String
     */
    public String getApellidos() {
        return this.apellidos;
    }

    
    /** 
     * @param apellidos
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    
    /** 
     * @return String
     */
    public String getDocumento() {
        return this.documento;
    }

    
    /** 
     * @param documento
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    
    /** 
     * @return Carrera
     */
    public Carrera getCarrera() {
        return this.carrera;
    }

    
    /** 
     * @param carrera
     */
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }


    
    /** 
     * @return Lista de categorías a las que pertenece el estudiante
     */
    public List<Categoria> getCategorias() {
        return this.categorias;
    }

    
    /** 
     * @param categorias
     */
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }
    
    /** 
     * @param o
     * @return boolean
     */
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

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nombres, apellidos, documento, carrera);
    }

    
    /** 
     * @return String
     */
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
