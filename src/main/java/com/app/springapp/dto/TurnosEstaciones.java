package com.app.springapp.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TurnosEstaciones {
    private class Horario implements Comparable<Horario> {
        private int id;
        private String descripcion;
        private List<TurnoDTO> turnos;

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescripcion() {
            return this.descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public List<TurnoDTO> getTurnos() {
            return this.turnos;
        }

        public void setTurnos(List<TurnoDTO> turnos) {
            this.turnos = turnos;
        }

        @Override
        public int compareTo(Horario otro) {
            if (id < otro.id) {
                return -1;
            }

            if (id > otro.id) {
                return 1;
            }

            return 0;
        }

    }

    private class Estacion implements Comparable<Estacion> {
        private int id;
        private String nombre;
        private List<Horario> horarios;

        private final HashMap<String, Integer> orden = new HashMap<>();

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombre() {
            return this.nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public List<Horario> getHorarios() {
            return this.horarios;
        }

        public void setHorarios(List<Horario> horarios) {
            this.horarios = horarios;
        }

        public Estacion() {
            this.orden.put("CyT", 1);
            this.orden.put("Calle 45", 2);
            this.orden.put("Calle 26", 3);
            this.orden.put("Uriel", 4);
            this.orden.put("Calle 53", 5);
        }

        @Override
        public int compareTo(Estacion otra) {
            if (!orden.containsKey(nombre) && !orden.containsKey(otra.nombre)) {
                return 0;
            }
            if (!orden.containsKey(nombre)) {
                return -1;
            }
            if (!orden.containsKey(otra.nombre)) {
                return 1;
            }

            if (orden.get(nombre) < orden.get(otra.nombre)) {
                return -1;
            }
            if (orden.get(nombre) > orden.get(otra.nombre)) {
                return 1;
            }
            return 0;
        }
    }

    private String fecha;
    private List<Estacion> estaciones;

    public TurnosEstaciones(String fecha) {
        this.fecha = fecha;
        this.estaciones = new ArrayList<>();
    }

    public String getFecha() {
        return this.fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Estacion> getEstaciones() {
        return this.estaciones;
    }

    public void setEstaciones(List<Estacion> estaciones) {
        this.estaciones = estaciones;
    }

    public void agregarTurno(TurnoDTO turno) {
        Estacion estacion = null;
        for (Estacion estIterator : this.estaciones) {
            if (estIterator.id == turno.idEstacion) {
                estacion = estIterator;
                break;
            }
        }

        if (estacion == null) {
            estacion = new Estacion();
            estacion.id = turno.idEstacion;
            estacion.nombre = turno.estacion;
            estacion.horarios = new ArrayList<>();

            this.estaciones.add(estacion);
            Collections.sort(this.estaciones);
        }

        Horario horario = null;
        for (Horario horIterator : estacion.horarios) {
            if (horIterator.id == turno.idHorario) {
                horario = horIterator;
                break;
            }
        }

        if (horario == null) {
            horario = new Horario();
            horario.id = turno.idHorario;
            horario.descripcion = turno.horario;
            horario.turnos = new ArrayList<>();

            estacion.horarios.add(horario);
            Collections.sort(estacion.horarios);
        }

        horario.turnos.add(turno);
        horario.turnos.sort(Comparator.nullsLast(Comparator.naturalOrder()));
    }

    public void agregarCuposDisponibles(com.app.springapp.entity.Estacion estacionEntity, com.app.springapp.entity.Horario horarioEntity, int numeroCupos) {
        Estacion estacion = null;
        for (Estacion estIterator : this.estaciones) {
            if (estIterator.id == estacionEntity.getId()) {
                estacion = estIterator;
                break;
            }
        }

        if (estacion == null) {
            estacion = new Estacion();
            estacion.id = (int)estacionEntity.getId();
            estacion.nombre = estacionEntity.getNombre();
            estacion.horarios = new ArrayList<>();

            this.estaciones.add(estacion);
            Collections.sort(this.estaciones);
        }

        Horario horario = null;
        for (Horario horIterator : estacion.horarios) {
            if (horIterator.id == horarioEntity.getId()) {
                horario = horIterator;
                break;
            }
        }

        if (horario == null) {
            horario = new Horario();
            horario.id = (int)horarioEntity.getId();
            horario.descripcion = horarioEntity.getDescripcion();
            horario.turnos = new ArrayList<>();

            estacion.horarios.add(horario);
            Collections.sort(estacion.horarios);
        }

        for (int i = 0; i < numeroCupos; i++) {
            horario.turnos.add(null);
        }
        horario.turnos.sort(Comparator.nullsLast(Comparator.naturalOrder()));
    }
}
