package com.app.springapp.dto;

import java.util.HashMap;

public class CuposYTurnosEstacionesHorario {
    HashMap<Long, HashMap<Long, Integer>> cupos = new HashMap<>();
    HashMap<Long, HashMap<Long, Integer>> turnos = new HashMap<>();

    public void putCupo(long estacionId, long horarioId, int sumCupos){
        if(!this.cupos.containsKey(estacionId))
            this.cupos.put(estacionId, new HashMap<Long, Integer>());
        
        this.cupos.get(estacionId).put(horarioId, sumCupos);
    }

    public void putTurno(long estacionId, long horarioId, int sumTurnos){
        if(!this.turnos.containsKey(estacionId))
            this.turnos.put(estacionId, new HashMap<Long, Integer>());
        
        this.turnos.get(estacionId).put(horarioId, sumTurnos);
    }

    /**
     * si no existe retorna null
     */
    public int getCupos(long estacionId, long horarioId){
        if(!this.cupos.containsKey(estacionId))
            return 0;

        Integer cupos = this.cupos.get(estacionId).get(horarioId);

        if(cupos == null)
            return 0;

        return cupos;
    }

    /**
     * si no existe retorna null
     */
    public int getTurnos(long estacionId, long horarioId){
        if(!this.turnos.containsKey(estacionId))
            return 0;

       Integer turnos = this.turnos.get(estacionId).get(horarioId);

       if(turnos == null)
           return 0;

       return turnos;
    }
}
