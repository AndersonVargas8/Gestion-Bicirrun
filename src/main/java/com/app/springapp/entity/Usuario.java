package com.app.springapp.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

/**
 * Esta clase define objetos para manejar la persistencia de usuarios de la aplicación.
 * @Version July 2022
 */
@Entity
public class Usuario implements Serializable {
    private static final long serialVersionUID = -2969524610059270447L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "usu_nombre")
    @NotBlank
    private String nombre;

    @Column(name = "usu_apellido")
    private String apellido;

    @Column(name = "usu_username", unique = true)
    @NotBlank
    private String username;

    @Column(name = "usu_password")
    @NotBlank
    private String password;

    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JoinColumn(name = "usu_rol")
    private Rol rol;

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String username, String password) {
        super();
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.password = password;
    }

    
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
    public String getApellido() {
        return this.apellido;
    }

    
    /** 
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    
    /** 
     * @return String
     */
    public String getUsername() {
        return this.username;
    }

    
    /** 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    
    /** 
     * @return String Retorna la contraseña cifrada
     */
    public String getPassword() {
        return this.password;
    }

    
    /** 
     * @param password contraseña sin cifrar
     */
    public void setPassword(String password) {
        this.password = password;
    }

    
    /** 
     * @return Role
     */
    public Rol getRol() {
        return this.rol;
    }

    
    /** 
     * @param rol
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                " id = '" + getId() + "'" +
                ", nombre = '" + getNombre() + "'" +
                ", apellido = '" + getApellido() + "'" +
                ", username = '" + getUsername() + "'" +
                ", password = '" + getPassword() + "'" +
                ", roles  = '" + getRol() + "'" +
                "}";
    }

    
    /** 
     * @param o objeto para ser comparado
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Usuario)) {
            return false;
        }
        Usuario usuario = (Usuario) o;
        return id == usuario.id && Objects.equals(nombre, usuario.nombre) && Objects.equals(apellido, usuario.apellido)
                && Objects.equals(username, usuario.username) && Objects.equals(rol, usuario.rol);
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, username, rol);
    }
}
