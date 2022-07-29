package com.app.springapp.interfacesServicios;

import java.util.HashMap;
import java.util.List;

import com.app.springapp.entity.Cupo;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;

public interface IServicioCupo {
    public List<Cupo> obtenerTodos();
    public List<Cupo> obtenerTodosViernes();
    public Cupo buscarPorEstacionYHorario(Estacion estacion, Horario horario);
    public List<Cupo> buscarPorHorario(Horario horario);
    public int cantidadCuposPorHorario(Horario horario);
    //public Cupo buscarPorCupoGrupo(int cupoGrupoId);
    //public int buscarIdPorCupoGrupoHorario(int idHorario);
    public Cupo buscarPorId(int id);
    //public Integer cantidadCupos();
    //public Integer cantidadCuposViernes();
    public List<Integer> idEstacionesPorHorario(int idHorario);

    /**
     * Retorna la cantidad de cupos que hay en cada día de la semana (Lunes a Viernes)
     * @return HashMap - clave: nombre del Dia, valor: cantidad de cupos
     */
    public HashMap<String, Integer> cantidadCuposAlDia();

    /**
     * @param dia
     * @param mes
     * @param anio
     * @return Cantidad de cupos que tiene una fecha específica
     */
    public int cantidadCuposPorFecha(int dia, int mes, int anio);

    /**
     * Retorna la cantidad de cupos de un cupo teniendo en cuenta si depende de otro cupo.
     * @param cupo
     * @return Número de cupos del cupo.
     */
    public int obtenerNumeroCupos(Cupo cupo);
}
