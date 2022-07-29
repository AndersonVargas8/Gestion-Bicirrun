package com.app.springapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TurnosCompletos implements Serializable{

    private int anio;
    private HashMap<Integer, Set<Integer>> meses;

    /**
     * Se inicializa con el año actual y una lista completa de los meses.
     */
    public TurnosCompletos(){
        this.anio = LocalDate.now().getYear();
        inicializarMeses();
    }

    /**
     * Se inicializa con el año especificado y una lista completa de los meses.
     * @param anio
     */
    public TurnosCompletos(int anio){
        this.anio = anio;
        inicializarMeses();
    }

    /**
     * Se inicializa con el año y la lista de meses especificados.
     * @param anio
     * @param meses
     */
    public TurnosCompletos(int anio, HashMap<Integer, Set<Integer>> meses){
        this.anio = anio;
        this.meses = meses;
    }

    private void inicializarMeses(){
        this.meses = new HashMap<>();

        this.meses.put(1, new HashSet<>());
        this.meses.put(2, new HashSet<>());
        this.meses.put(3, new HashSet<>());
        this.meses.put(4, new HashSet<>());
        this.meses.put(5, new HashSet<>());
        this.meses.put(6, new HashSet<>());
        this.meses.put(7, new HashSet<>());
        this.meses.put(8, new HashSet<>());
        this.meses.put(9, new HashSet<>());
        this.meses.put(10, new HashSet<>());
        this.meses.put(11, new HashSet<>());
        this.meses.put(12, new HashSet<>());
    } 
    
    /**
     * Se agrega el día a la lista de días con turnos completos en el mes especificado.
     * @param mes
     * @param dia
     * @throws IllegalArgumentException El mes especificado no existe en el registro.
     * @throws IllegalArgumentException El día especificado ya está completo.
     */
    public void agregarDiaCompleto(int mes, int dia){
        validarMes(mes);

        Set<Integer> diasCompletos = this.meses.get(mes);

        if(diasCompletos.contains(dia)){
            throw new IllegalArgumentException("El día especificado ya está completo");
        }

        diasCompletos.add(dia);
    }

    /**
     * Elimina el día especificado del conjunto de días completos del mes.
     * @param mes
     * @param dia
     * @throws IllegalArgumentException El mes especificado no existe en el registro.
     * @throws IllegalArgumentException El día especificado no está completo.
     */
    public void eliminarDiaCompleto(int mes, int dia){
        validarMes(mes);

        Set<Integer> diasCompletos = this.meses.get(mes);

        if(!diasCompletos.contains(dia)){
            throw new IllegalArgumentException("El día especificado no está completo");
        }

        diasCompletos.remove(dia);
    }

    /**
     * 
     * @param mes
     * @param dia
     * @return True si el día especificado tiene los turnos completos. False en otro caso.
     * @throws IllegalArgumentException El mes especificado no existe en el registro.
     */
    public boolean estaCompleto(int mes, int dia){
        validarMes(mes);
        
        Set<Integer> diasCompletos = this.meses.get(mes);

        if(diasCompletos.contains(dia)){
            return true;
        }

        return false; 
    }

    /**
     * 
     * @param mes
     * @return Suma total de días completos del mes especificado.
     */
    public int numeroDiasCompletos(int mes){
        validarMes(mes);

        Set<Integer> diasCompletos = this.meses.get(mes);

        return diasCompletos.size();
    }

    /**
     * 
     * @param mes
     * @return Conjunto de días completos del mes especificado.
     */
    public Set<Integer> diasCompletosMes(int mes){
       validarMes(mes);
        
        return this.meses.get(mes);
    }

    private boolean validarMes(int mes){
        if(!this.meses.containsKey(mes)){
            throw new IllegalArgumentException("El mes especificado no existe en el registro");
        }

        return true;
    }


    public int getAnio() {
        return this.anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public HashMap<Integer,Set<Integer>> getMeses() {
        return this.meses;
    }

    public void setMeses(HashMap<Integer,Set<Integer>> meses) {
        this.meses = meses;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TurnosCompletos)) {
            return false;
        }
        TurnosCompletos turnosCompletos = (TurnosCompletos) o;
        return anio == turnosCompletos.anio && Objects.equals(meses, turnosCompletos.meses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(anio, meses);
    }


    @Override
    public String toString() {
        return "{" +
            " anio='" + getAnio() + "'" +
            ", meses='" + getMeses() + "'" +
            "}";
    }

    
}
