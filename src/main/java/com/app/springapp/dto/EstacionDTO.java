package com.app.springapp.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EstacionDTO {
    private Integer id;
    private String nombre;
    private boolean is_habilitada;

    @JsonCreator
    public EstacionDTO(@JsonProperty("id") Integer id, @JsonProperty("nombre") String nombre, @JsonProperty("is_habilitada") boolean is_habilitada) {
        this.id = id;
        this.nombre = nombre;
        this.is_habilitada = is_habilitada;
    }
    
    public EstacionDTO(Integer id) {
        this.id = id;
    }

    public EstacionDTO(String nombre) {
        this.nombre = nombre;
    }

    public EstacionDTO(boolean is_habilitada) {
        this.is_habilitada = is_habilitada;
    }

    public EstacionDTO(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public EstacionDTO(Integer id, boolean is_habilitada) {
        this.id = id;
        this.is_habilitada = is_habilitada;
    }

    public EstacionDTO(String nombre, boolean is_habilitada) {
        this.nombre = nombre;
        this.is_habilitada = is_habilitada;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getIs_habilitada() {
        return is_habilitada;
    }

    public void setIs_habilitada(boolean is_habilitada) {
        this.is_habilitada = is_habilitada;
    }

}
