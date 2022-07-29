package com.app.springapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

/**
 * Esta clase permite manejar la persistencia de los turnos programados en cada
 * día de cada mes.
 */
public class TurnosProgramados implements Serializable {
    private int anio;
    private HashMap<Integer,Mes> meses;
    
    /**
     * Se inicializa con el año actual y un diccionario con la lista de meses
     */
    public TurnosProgramados() {
        this.anio = LocalDate.now().getYear();
        this.inicializarMeses();
    }

    public TurnosProgramados(int anio){
        this.anio = anio;
        this.inicializarMeses();
    }

    private void inicializarMeses(){
        this.meses = new HashMap<>();

        this.meses.put(1, new Mes("enero"));
        this.meses.put(2, new Mes("febrero"));
        this.meses.put(3, new Mes("marzo"));
        this.meses.put(4, new Mes("abril"));
        this.meses.put(5, new Mes("mayo"));
        this.meses.put(6, new Mes("junio"));
        this.meses.put(7, new Mes("julio"));
        this.meses.put(8, new Mes("agosto"));
        this.meses.put(9, new Mes("septiembre"));
        this.meses.put(10, new Mes("octubre"));
        this.meses.put(11, new Mes("noviembre"));
        this.meses.put(12, new Mes("diciembre"));
    }

    public TurnosProgramados(int anio, HashMap<Integer,Mes> meses) {
        this.anio = anio;
        this.meses = meses;
    }

    
    /** 
     * @return int
     */
    public int getAnio() {
        return this.anio;
    }

    
    /** 
     * @param anio
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    
    /** 
     * @return HashMap<String, Mes>
     */
    public HashMap<Integer,Mes> getMeses() {
        return this.meses;
    }

    
    /** 
     * @param meses
     */
    public void setMeses(HashMap<Integer,Mes> meses) {
        this.meses = meses;
    }

    /**
     * Obtener un mes especifico, con los turnos programados en cada día.
     * @param mes Nombre del mes. No distingue mayúsculas de minúsculas.
     * @return Mes
     * @throws IllegalArgumentException El mes especificado no existe en el registro
     */
    public Mes getMes(int mes){
        validarMes(mes);
        
        return this.meses.get(mes); 
    }
    
    /**
     * Suma un turno a un día en un mes especificado
     * @param mes Nombre del mes. No distingue mayúsculas de minúsculas.
     * @param dia
     * @return El nuevo número de turnos que contiene el día especificado
     * @throws IllegalArgumentException El mes especificado no existe en el registro
     */
    public int agregarTurno(int mes, int dia){
        validarMes(mes);
        Mes mesObjetivo = getMes(mes);
        return mesObjetivo.agregarTurno(dia);
    }

    /**
     * Establece el número de turnos de un día específico en un mes específico.
     * @param mes
     * @param dia
     * @param numeroTurnos
     * @throws IllegalArgumentException El mes especificado no existe en el registro
     */
    public void establecerTurnosDia(int mes, int dia, int numeroTurnos){
        validarMes(mes);
        Mes mesObjetivo = getMes(mes);
        mesObjetivo.establecerTurnosDia(dia, numeroTurnos);
    }

    /**
     * Reduce en uno el número de turnos programados en un día y mes especificados.
     * @param mes
     * @param dia
     * @return El nuevo número de turnos del día y mes especificados.
     * @throws IllegalArgumentException El mes especificado no existe en el registro
     */
    public int eliminarTurno(int mes, int dia){
        validarMes(mes);
        Mes mesObjetivo = getMes(mes);
        return mesObjetivo.eliminarTurno(dia);
    }

    /**
     * 
     * @param mes
     * @return La suma total de turnos programados en el mes especificado.
     */
    public int numeroTurnosMes(int mes){
        validarMes(mes);
        Mes mesObjetivo = getMes(mes);
        return mesObjetivo.numeroTurnos();
    }

    /**
     * 
     * @param mes
     * @param dia
     * @return El número de turnos programados en el mes especificado.
     */
    public int numeroTurnosDia(int mes, int dia){
        validarMes(mes);
        Mes mesObjetivo = getMes(mes);
        return mesObjetivo.numeroTurnosDia(dia);
    }

    private boolean validarMes(int mes){
        if(!this.meses.containsKey(mes)){
            throw new IllegalArgumentException("El mes especificado no existe en el registro");
        }

        return true;
    }
    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TurnosProgramados)) {
            return false;
        }
        TurnosProgramados turnosProgramados = (TurnosProgramados) o;
        return anio == turnosProgramados.anio && Objects.equals(meses, turnosProgramados.meses);
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(anio, meses);
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
            " anio='" + getAnio() + "'" +
            ", meses='" + getMeses() + "'" +
            "}";
    }

    
}
