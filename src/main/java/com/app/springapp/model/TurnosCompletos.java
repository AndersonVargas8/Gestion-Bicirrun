package com.app.springapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TurnosCompletos implements Serializable {
    class Mes implements Serializable {
        Set<Integer> diasCompletos;
        HashMap<Integer, Set<Integer>> diasCompletosHorario;

        public Mes() {
            this.diasCompletos = new HashSet<>();
            this.diasCompletosHorario = new HashMap<>();
        }

        @Override
        public String toString() {
            return "\n{" +
                    " dias completos='" + diasCompletos + "'" +
                    ", \ndias completos por horario='" + diasCompletosHorario + "'" +
                    "\n}";
        }

    }

    private int anio;
    private HashMap<Integer, Mes> meses;

    /**
     * Se inicializa con el año actual y una lista completa de los meses.
     */
    public TurnosCompletos() {
        this.anio = LocalDate.now().getYear();
        inicializarMeses();
    }

    /**
     * Se inicializa con el año especificado y una lista completa de los meses.
     * 
     * @param anio
     */
    public TurnosCompletos(int anio) {
        this.anio = anio;
        inicializarMeses();
    }

    private void inicializarMeses() {
        this.meses = new HashMap<>();

        this.meses.put(1, new Mes());
        this.meses.put(2, new Mes());
        this.meses.put(3, new Mes());
        this.meses.put(4, new Mes());
        this.meses.put(5, new Mes());
        this.meses.put(6, new Mes());
        this.meses.put(7, new Mes());
        this.meses.put(8, new Mes());
        this.meses.put(9, new Mes());
        this.meses.put(10, new Mes());
        this.meses.put(11, new Mes());
        this.meses.put(12, new Mes());
    }
    
    /**
     * Se agrega el día a la lista de días con turnos completos en el mes
     * especificado.
     * 
     * @param mes
     * @param dia
     * @throws IllegalArgumentException El mes especificado no existe en el
     *                                  registro.
     * @throws IllegalArgumentException El día especificado ya está completo.
     */
    public void agregarDiaCompleto(int mes, int dia) {
        validarMes(mes);

        Set<Integer> diasCompletos = this.meses.get(mes).diasCompletos;

        if (diasCompletos.contains(dia)) {
            throw new IllegalArgumentException("El día especificado ya está completo");
        }

        diasCompletos.add(dia);
    }

    /**
     * Se agrega un dia al conjunto de días completos en el horario especificado
     * 
     * @param mes
     * @param dia
     * @param idHorario
     */
    public void agregarDiaCompletoHorario(int mes, int dia, int idHorario) {
        validarMes(mes);
        HashMap<Integer, Set<Integer>> diasCompletosHorarios = this.meses.get(mes).diasCompletosHorario;

        // si no existe el horario aún
        if (!diasCompletosHorarios.containsKey(idHorario)) {
            Set<Integer> set = new HashSet<>();
            set.add(dia);
            diasCompletosHorarios.put(idHorario, set);
            return;
        }

        // si el día y el horario ya existen
        if (diasCompletosHorarios.get(idHorario).contains(dia)) {
            throw new IllegalArgumentException("El día especificado ya está completo");
        }

        // si el horario existe pero el día no
        diasCompletosHorarios.get(idHorario).add(dia);
    }

    /**
     * Elimina el día especificado del conjunto de días completos del mes.
     * 
     * @param mes
     * @param dia
     * @throws IllegalArgumentException El mes especificado no existe en el
     *                                  registro.
     * @throws IllegalArgumentException El día especificado no está completo.
     */
    public void eliminarDiaCompleto(int mes, int dia) {
        validarMes(mes);

        Set<Integer> diasCompletos = this.meses.get(mes).diasCompletos;

        if (!diasCompletos.contains(dia)) {
            throw new IllegalArgumentException("El día especificado no está completo");
        }

        diasCompletos.remove(dia);
    }

    /**
     * Elimina un día completo en un horario específico
     * 
     * @param mes
     * @param dia
     * @param idHorario
     */
    public void eliminarDiaCompletoHorario(int mes, int dia, int idHorario) {
        validarMes(mes);
        HashMap<Integer, Set<Integer>> diasCompletosHorarios = this.meses.get(mes).diasCompletosHorario;

        // si no contiene el horario o no contiene el día
        if (!diasCompletosHorarios.containsKey(idHorario) || !diasCompletosHorarios.get(idHorario).contains(dia)) {
            throw new IllegalArgumentException("El día especificado no está completo");
        }

        diasCompletosHorarios.get(idHorario).remove(dia);
    }

    /**
     * 
     * @param mes
     * @param dia
     * @return True si el día especificado tiene los turnos completos. False en otro
     *         caso.
     * @throws IllegalArgumentException El mes especificado no existe en el
     *                                  registro.
     */
    public boolean estaCompleto(int mes, int dia) {
        validarMes(mes);

        Set<Integer> diasCompletos = this.meses.get(mes).diasCompletos;

        return diasCompletos.contains(dia);

    }

    /**
     * 
     * @param mes
     * @param dia
     * @param idHorario
     * @return True si el día está completo en el horario especificado. False en
     *         otro caso
     */
    public boolean estaCompletoHorario(int mes, int dia, int idHorario) {
        validarMes(mes);
        HashMap<Integer, Set<Integer>> diasCompletosHorarios = this.meses.get(mes).diasCompletosHorario;

        if(!diasCompletosHorarios.containsKey(idHorario))
            return false;

        return diasCompletosHorarios.get(idHorario).contains(dia);
    }

    /**
     * 
     * @param mes
     * @return Suma total de días completos del mes especificado.
     */
    public int numeroDiasCompletos(int mes) {
        validarMes(mes);

        Set<Integer> diasCompletos = this.meses.get(mes).diasCompletos;

        return diasCompletos.size();
    }

    /**
     * 
     * @param mes
     * @return Conjunto de días completos del mes especificado.
     */
    public Set<Integer> diasCompletosMes(int mes) {
        validarMes(mes);

        return this.meses.get(mes).diasCompletos;
    }

    /**
     * 
     * @param mes
     * @param idHorario
     * @return Conjunto de días completos del mes y horario especificados
     */
    public Set<Integer> diasCompletosMesHorario(int mes, int idHorario) {
        validarMes(mes);
        HashMap<Integer, Set<Integer>> diasCompletosHorarios = this.meses.get(mes).diasCompletosHorario;

        if (diasCompletosHorarios.containsKey(idHorario))
            return diasCompletosHorarios.get(idHorario);

        return new HashSet<Integer>();
    }

    private boolean validarMes(int mes) {
        if (!this.meses.containsKey(mes)) {
            throw new IllegalArgumentException("El mes especificado no existe en el registro");
        }

        return true;
    }

    public Set<Integer> getMesesRegistrados() {
        return this.meses.keySet();
    }

    public int getAnio() {
        return this.anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public HashMap<Integer, Mes> getMeses() {
        return this.meses;
    }

    public void setMeses(HashMap<Integer, Mes> meses) {
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
