package com.app.springapp.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
 * Esta clase define objetos para manejar la persistencia de usuarios de la aplicación.
 * @Version July 2022
 */
@Entity
public class Usuario implements Serializable {
    private static final long serialVersionUID = -2969524610059270447L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotBlank
    private String username;

    @Column
    @NotBlank
    private String password;

    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REFRESH }, fetch = FetchType.EAGER)
    @JoinColumn(name = "usu_rol")
    private Rol rol;

    @ManyToMany
    @JoinTable(name = "usuarios_estaciones",
    joinColumns = @JoinColumn(name = "usuario_id"),
    inverseJoinColumns = @JoinColumn(name = "estacion_id"))
    private Set<Estacion> estaciones;

    public Usuario() {
    }

    public Usuario(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    
    
    public Usuario(@NotBlank String username, @NotBlank String password, Rol rol, Set<Estacion> categorias) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.estaciones = categorias;
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

    
    
    public void setId(long id) {
        this.id = id;
    }

    public Set<Estacion> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(Set<Estacion> estaciones) {
        this.estaciones = estaciones;
    }

    public void addEstacion(Estacion estacion){
        if(this.estaciones == null){
            this.estaciones = new HashSet<>();
        }

        this.estaciones.add(estacion);
    }

    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                " id = '" + getId() + "'" +
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
        return id == usuario.id && Objects.equals(username, usuario.username) && Objects.equals(rol, usuario.rol);
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, rol);
    }
}
