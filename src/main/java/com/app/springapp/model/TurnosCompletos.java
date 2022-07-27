package com.app.springapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TurnosCompletos implements Serializable{

    private int anio;
    private HashMap<String, Set<Integer>> meses;

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
    public TurnosCompletos(int anio, HashMap<String, Set<Integer>> meses){
        this.anio = anio;
        this.meses = meses;
    }

    private void inicializarMeses(){
        this.meses = new HashMap<>();

        this.meses.put("enero", new HashSet<>());
        this.meses.put("febrero", new HashSet<>());
        this.meses.put("marzo", new HashSet<>());
        this.meses.put("abril", new HashSet<>());
        this.meses.put("mayo", new HashSet<>());
        this.meses.put("junio", new HashSet<>());
        this.meses.put("julio", new HashSet<>());
        this.meses.put("agosto", new HashSet<>());
        this.meses.put("septiembre", new HashSet<>());
        this.meses.put("octubre", new HashSet<>());
        this.meses.put("noviembre", new HashSet<>());
        this.meses.put("diciembre", new HashSet<>());
    } 
    
    /**
     * Se agrega el día a la lista de días con turnos completos en el mes especificado.
     * @param mes
     * @param dia
     * @throws IllegalArgumentException El mes especificado no existe en el registro.
     * @throws IllegalArgumentException El día especificado ya está completo.
     */
    public void agregarDiaCompleto(String mes, int dia){
        mes = validarNombreMes(mes);

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
    public void eliminarDiaCompleto(String mes, int dia){
        mes = validarNombreMes(mes);

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
    public boolean estaCompleto(String mes, int dia){
        mes = validarNombreMes(mes);
        
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
    public int numeroDiasCompletos(String mes){
        mes = validarNombreMes(mes);

        Set<Integer> diasCompletos = this.meses.get(mes);

        return diasCompletos.size();
    }

    /**
     * 
     * @param mes
     * @return Conjunto de días completos del mes especificado.
     */
    public Set<Integer> diasCompletosMes(String mes){
        mes = validarNombreMes(mes);
        
        return this.meses.get(mes);
    }

    private String validarNombreMes(String mes){
        mes = mes.toLowerCase();
        if(!this.meses.containsKey(mes)){
            throw new IllegalArgumentException("El mes especificado no existe en el registro");
        }

        return mes;
    }


    public int getAnio() {
        return this.anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public HashMap<String,Set<Integer>> getMeses() {
        return this.meses;
    }

    public void setMeses(HashMap<String,Set<Integer>> meses) {
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
