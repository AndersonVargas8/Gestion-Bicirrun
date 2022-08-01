package com.app.springapp.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Esta clase permite transportar la información de turnos programados en cada
 * dia de un mes
 */
public class Calendario {
    public int anio;
    public int mes;
    public String nombreMes;

    public List<Semana> semanas;

    public Calendario(int mes, int anio) {
        this.anio = anio;
        this.mes = mes;
        String nombreMes = "";

        switch (mes) {
            case 1:
                nombreMes = "Enero";
                break;
            case 2:
                nombreMes = "Febrero";
                break;
            case 3:
                nombreMes = "Marzo";
                break;
            case 4:
                nombreMes = "Abril";
                break;
            case 5:
                nombreMes = "Mayo";
                break;
            case 6:
                nombreMes = "Junio";
                break;
            case 7:
                nombreMes = "Julio";
                break;
            case 8:
                nombreMes = "Agosto";
                break;
            case 9:
                nombreMes = "Septiembre";
                break;
            case 10:
                nombreMes = "Octubre";
                break;
            case 11:
                nombreMes = "Noviembre";
                break;
            case 12:
                nombreMes = "Diciembre";
                break;
        }

        this.nombreMes = nombreMes;

        this.semanas = new ArrayList<Semana>();
    }

    public void agregarSemana() {
        this.semanas.add(new Semana());
    }

    public void agregarDiaSemana(int numSemana, int numeroDiaSemana, int numeroDia, int cupos, int turnosProgramados) {
        if (semanas.isEmpty() || numeroDiaSemana == 1)
            agregarSemana();
        Semana semana = this.semanas.get(numSemana);
        semana.agregarDia(numeroDiaSemana, numeroDia, cupos, turnosProgramados);
    }

    /**
     * Convierte un número dado a un nombre de día. De 1(Lunes) a 7(Domingo)
     * 
     * @param numeroDia
     */
    public static String convertirNumeroADia(int numeroDia) {
        String nombre = "";

        switch (numeroDia) {
            case 1:
                nombre = "Lunes";
                break;
            case 2:
                nombre = "Martes";
                break;
            case 3:
                nombre = "Miércoles";
                break;
            case 4:
                nombre = "Jueves";
                break;
            case 5:
                nombre = "Viernes";
                break;
            case 6:
                nombre = "Sábado";
                break;
            case 7:
                nombre = "Domingo";
                break;
        }

        return nombre;
    }

    /**
     * Convierte el nombre de un dia (por ejemplo 'lunes') al número correspondiente
     * en la lista de dias.
     * Lunes = 1
     * Martes = 2
     * Miercoles = 3
     * Jueves = 4
     * Viernes = 5
     */
    public static int convertirNombreDiaANumeroSemana(String nombreDia) {
        nombreDia = nombreDia.toLowerCase();
        if (nombreDia.equals("lunes"))
            return 1;
        if (nombreDia.equals("martes"))
            return 2;
        if (nombreDia.equals("miercoles") || nombreDia.equals("miércoles"))
            return 3;
        if (nombreDia.equals("jueves"))
            return 4;
        if (nombreDia.equals("viernes"))
            return 5;

        return -1;
    }

    public class Semana {
        public static final int LUNES = 1;
        public static final int MARTES = 2;
        public static final int MIERCOLES = 3;
        public static final int JUEVES = 4;
        public static final int VIERNES = 5;

        public List<Dia> dias;

        public Semana() {
            this.dias = new ArrayList<Dia>();

            for (int i = 0; i < 5; i++) {
                dias.add(null);
            }
        }

        /**
         * Se agrega un día a la semana
         * 
         * @param nombreDia         nombre del día que se quiere agregar {lunes, martes,
         *                          miercoles, jueves, viernes}
         * @param numeroDia         número del día
         * @param cupos
         * @param turnosProgramados
         * @throws IllegalArgumentException El día ingresado es incorrecto
         */
        public void agregarDia(String nombreDia, int numeroDia, int cupos, int turnosProgramados) {
            nombreDia = nombreDia.toLowerCase();
            if (!Arrays.asList("lunes", "martes", "miercoles", "miércoles", "jueves", "viernes").contains(nombreDia)) {
                throw new IllegalArgumentException("El día ingresado es incorrecto");
            }

            int numeroSemana = convertirNombreDiaANumeroSemana(nombreDia);

            this.dias.set(--numeroSemana, new Dia(numeroDia, cupos, turnosProgramados));
        }

        /**
         * Agrega un día a la semana en la posición (numeroDiasemana) especificado
         * 
         * @param numeroDiaSemana   numero de día correspondiente en la semana. (0 =
         *                          lunes, 1= martes, ...)
         * @param numeroDia
         * @param cupos
         * @param turnosProgramados
         * @throws IllegalArgumentException "El número de dia en la semana no es válido.
         *                                  Debe estar entre 1 (Lunes) y 5(Viernes)"
         */
        public void agregarDia(int numeroDiaSemana, int numeroDia, int cupos, int turnosProgramados) {
            if (numeroDiaSemana < 1 || numeroDiaSemana > 5) {
                throw new IllegalArgumentException("El número de dia en la semana no es válido. Debe estar en [0,4]");
            }
            this.dias.set(--numeroDiaSemana, new Dia(numeroDia, cupos, turnosProgramados));
        }

    }

    public class Dia {
        public int numeroDia;
        public int cupos;
        public int turnosProgramados;

        /**
         * Puede ser:
         * - vencido: si el dia es menor a la fecha actual.
         * - completo: si el número de turnos programados es igual al número de cupos.
         * - disponible: si el número de turnos programados es menor al número de cupos.
         * - medio: si la diferencia entre cupos y turnos programados es menor o igual a
         * 5
         */
        public String estado;

        /**
         * Crea el día y establece el estado en función de los cupos y turnos
         * programados
         * 
         * @param numeroDia
         * @param cupos
         * @param turnosProgramados
         */
        public Dia(int numeroDia, int cupos, int turnosProgramados) {
            if (cupos < turnosProgramados) {
                throw new IllegalArgumentException(
                        "El número de turnos programados no puede ser mayor al número de cupos");
            }
            this.numeroDia = numeroDia;
            this.cupos = cupos;
            this.turnosProgramados = turnosProgramados;

            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaDia = LocalDate.of(anio, mes, numeroDia);

            if (fechaActual.compareTo(fechaDia) > 0) {
                this.estado = "vencido";
            } else if (this.cupos == this.turnosProgramados) {
                this.estado = "completo";
            } else if ((this.cupos - this.turnosProgramados) <= 5) {
                this.estado = "medio";
            } else {
                this.estado = "disponible";
            }
        }

    }
}
