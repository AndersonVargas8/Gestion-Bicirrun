package com.app.springapp.dto;

public class EstudianteDTO {
    public long id;
	public String nombres;
	public String apellidos;
	public String documento;
    public int carrera;
    public String carreraNombre;
    public String telefono;

    public EstudianteDTO(){
        
    }

    public EstudianteDTO(long id, String nombres, String apellidos, String documento, int carrera, String carreraNombre, String telefono){
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.documento = documento;
        this.carrera = carrera;
        this.carreraNombre = carreraNombre;
        this.telefono = telefono;
    }

    public EstudianteDTO(long id, String nombres, String apellidos){
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }
}
