package com.app.springapp.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * Esta clase permite manejar la persistencia de los cupos que tiene cada
 * estación.
 * Un cupo está compuesto por una estación un horario y una número de cupos
 */
@Entity
public class Cupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "est_id")
    private Estacion estacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hor_id")
    private Horario horario;

    @Column
    private int num_cupos;

    // Creación de la tabla de cupos compartidos, cuando dos horarios comparten los
    // mismos cupos
    @ManyToMany
    @JoinTable(name = "cupos_compartidos", joinColumns = @JoinColumn(name = "cupo_dependiente"), inverseJoinColumns = @JoinColumn(name = "cupo_independiente"))
    private List<Cupo> cupos_independientes;

    @ManyToMany(mappedBy = "cupos_independientes")
    private List<Cupo> cupos_dependientes;

    public Cupo(long id, Estacion estacion, Horario horario, int num_cupos, List<Cupo> cupos_independientes,
            List<Cupo> cupos_dependientes) {
        this.id = id;
        this.estacion = estacion;
        this.horario = horario;
        this.num_cupos = num_cupos;
        this.cupos_independientes = cupos_independientes;
        this.cupos_dependientes = cupos_dependientes;
    }

    public Cupo() {
    }

    public Cupo(long id, Estacion estacion, Horario horario, int num_cupos) {
        this.id = id;
        this.estacion = estacion;
        this.horario = horario;
        this.num_cupos = num_cupos;
    }

    /**
     * @return long
     */
    public long getId() {
        return this.id;
    }

    /**
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return Estacion
     */
    public Estacion getEstacion() {
        return this.estacion;
    }

    /**
     * @param estacion
     */
    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }

    /**
     * @return Horario
     */
    public Horario getHorario() {
        return this.horario;
    }

    /**
     * @param horario
     */
    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    /**
     * @return int
     */
    public int getNum_cupos() {
        return this.num_cupos;
    }

    /**
     * @param num_cupos
     */
    public void setNum_cupos(int num_cupos) {
        this.num_cupos = num_cupos;
    }

    /**
     * @return List<Cupo>
     */
    public List<Cupo> getCupos_independientes() {
        return this.cupos_independientes;
    }

    /**
     * @param cupos_independientes
     */
    public void setCupos_independientes(List<Cupo> cupos_independientes) {
        this.cupos_independientes = cupos_independientes;
    }

    /**
     * @return List<Cupo>
     */
    public List<Cupo> getCupos_dependientes() {
        return this.cupos_dependientes;
    }

    /**
     * @param cupos_dependientes
     */
    public void setCupos_dependientes(List<Cupo> cupos_dependientes) {
        this.cupos_dependientes = cupos_dependientes;
    }

    /**
     * @return True si tiene algún cupo dependiente o algún cupo indepeniente. False
     *         en otro caso.
     */
    public boolean tieneCupoCompartido() {
        if(cupos_dependientes.isEmpty() && cupos_independientes.isEmpty()){
            return false;
        };
        if(!cupos_dependientes.isEmpty() || !cupos_independientes.isEmpty()){
            return true;
        };

        return false; 
    }

    public Cupo getCupoCompartido() {
        if (tieneCupoCompartido()) {
            if (!cupos_dependientes.isEmpty()) {
                return cupos_dependientes.get(0);
            }

            if (!cupos_independientes.isEmpty()) {
                return cupos_independientes.get(0);
            }
        }
        return null;
    }

    /**
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Cupo)) {
            return false;
        }
        Cupo cupo = (Cupo) o;
        return id == cupo.id && Objects.equals(estacion, cupo.estacion) && Objects.equals(horario, cupo.horario)
                && num_cupos == cupo.num_cupos;
    }

    /**
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, estacion, horario, num_cupos);
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", estacion='" + getEstacion() + "'" +
                ", horario='" + getHorario() + "'" +
                ", num_cupos='" + getNum_cupos() + "'" +
                "}";
    }

}
