package com.app.springapp.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="cupo",schema="public")
public class Cupo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "est_id")
    private Estacion estacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hor_id")
    private Horario horario;

    @Column(columnDefinition = "bigint(20) default '0'")
    private int num_cupos;

    
    @Column(name = "cupo_grupo", columnDefinition = "bigint(20) default '0'") 
    private int cupoGrupo; 
    

    public Cupo() {
    }

    public Cupo(long id, Estacion estacion, Horario horario, int num_cupos, int cupoGrupo) {
        this.id = id;
        this.estacion = estacion;
        this.horario = horario;
        this.num_cupos = num_cupos;
        this.cupoGrupo = cupoGrupo;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Estacion getEstacion() {
        return this.estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }

    public Horario getHorario() {
        return this.horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public int getNum_cupos() {
        return this.num_cupos;
    }

    public void setNum_cupos(int num_cupos) {
        this.num_cupos = num_cupos;
    }

    public int getCupoGrupo() {
        return this.cupoGrupo;
    }

    public void setCupoGrupo(int cupoGrupo) {
        this.cupoGrupo = cupoGrupo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Cupo)) {
            return false;
        }
        Cupo cupo = (Cupo) o;
        return id == cupo.id && Objects.equals(estacion, cupo.estacion) && Objects.equals(horario, cupo.horario) && num_cupos == cupo.num_cupos && cupoGrupo == cupo.cupoGrupo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, estacion, horario, num_cupos, cupoGrupo);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", estacion='" + getEstacion() + "'" +
            ", horario='" + getHorario() + "'" +
            ", num_cupos='" + getNum_cupos() + "'" +
            ", cupoGrupo='" + getCupoGrupo() + "'" +
            "}";
    }

}
