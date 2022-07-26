package com.app.springapp.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Esta clase permite manejar la cantidad de turnos que hay en los diferentes
 * días de un mes específico.
 */
public class Mes implements Serializable{
    /**
     * Nombre del mes
     */
    private String nombre;
    /**
     * Diccionario que contiene los turnos programados en cada día.
     * La clave es el número del día y el valor es la cantidad de turnos.
     */
    private HashMap<Integer, Integer> turnosDias;

    /**
     * Inicializa el mes con el nombre especificado y un diccionario de turnos
     * programados vacío.
     * 
     * @param nombre
     */
    public Mes(String nombre) {
        this.nombre = nombre;
        this.turnosDias = new HashMap<>();
    }

    /**
     * Inicializa el mes con el nombre especificado y un diccionario de turnos
     * programados con una entrada
     * correspondiente al día y cantidad de turnos especificados.
     * 
     * @param nombre
     * @param dia
     * @param cantidadTurnos
     */
    public Mes(String nombre, int dia, int cantidadTurnos) {
        this.nombre = nombre;
        this.turnosDias = new HashMap<>();
        this.turnosDias.put(dia, cantidadTurnos);
    }

    /**
     * Inicializa el mes con un nombre especificado y un diccionario de turnos
     * programados especificado.
     * 
     * @param nombre
     * @param turnosDias
     */
    public Mes(String nombre, HashMap<Integer, Integer> turnosDias) {
        this.nombre = nombre;
        this.turnosDias = turnosDias;
    }

    /**
     * Suma un turno progrmado al día especificado.
     * 
     * @param dia
     * @return El nuevo número de turnos programados en el día.
     */
    public int agregarTurno(int dia) {
        int numeroTurnos;

        if (!this.turnosDias.containsKey(dia)) {
            numeroTurnos = 0;
        } else {
            numeroTurnos = turnosDias.get(dia);
        }

        this.turnosDias.put(dia, ++numeroTurnos);

        return numeroTurnos;
    }

    /**
     * Establece el número de turnos de un día específico.
     * 
     * @param dia
     * @param turnos
     * @throws IllegalArgumentException El número de turnos es negativo.
     */
    public void establecerTurnosDia(int dia, int numeroTurnos) {
        if (numeroTurnos < 0) {
            throw new IllegalArgumentException("La cantidad de turnos no puede ser negativa");
        }

        this.turnosDias.put(dia, numeroTurnos);
    }

    /**
     * Reduce en uno los turnos programados en el día especificado.
     * 
     * @param dia
     * @return El nuevo número de turnos programados en el día especificado.
     * @throws UnsupportedOperationException El día especificado tiene cero turnos
     *                                       programados.
     */
    public int eliminarTurno(int dia) {
        if (!this.turnosDias.containsKey(dia) || this.turnosDias.get(dia) == 0) {
            throw new UnsupportedOperationException("El número de turnos es actualmente cero");
        }

        int numeroTurnos = turnosDias.get(dia);

        turnosDias.put(dia, --numeroTurnos);

        return numeroTurnos;
    }

    /**
     * Establece el número de turnos programados en el día especificado en cero.
     * @param dia
     */
    public void reiniciarTurnos(int dia){
        if(this.turnosDias.containsKey(dia)){
            establecerTurnosDia(dia, 0);
        }
    }

    /**
     * @param dia
     * @return El número de turnos programados en el día especificado.
     */
    public int numeroTurnosDia(int dia) {
        int numeroTurnos = (this.turnosDias.containsKey(dia)) ? this.turnosDias.get(dia) : 0;

        return numeroTurnos;
    }

    /**
     * 
     * @return Ls suma total de turnos programados en el mes.
     */
    public int numeroTurnos(){
        int suma = 0;
        for(Integer turnos: this.turnosDias.values()){
            suma += turnos;
        }

        return suma;
    }

    /**
     * @return String
     */
    public String getNombre() {
        return this.nombre ;
    }

    /**
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return HashMap<Integer, Integer>
     */
    public HashMap<Integer, Integer> getTurnosDias() {
        return this.turnosDias;
    }

    /**
     * @param turnosDias
     */
    public void setTurnosDias(HashMap<Integer, Integer> turnosDias) {
        this.turnosDias = turnosDias;
    }


    
    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Mes)) {
            return false;
        }
        Mes mes = (Mes) o;
        return Objects.equals(nombre, mes.nombre) && Objects.equals(turnosDias, mes.turnosDias);
    }

    
    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre, turnosDias);
    }


    
    /** 
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
            " nombre='" + getNombre() + "'" +
            ", turnosDias='" + getTurnosDias().toString() + "'" +
            "}";
    }

}
