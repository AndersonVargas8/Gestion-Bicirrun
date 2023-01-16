package com.app.springapp.dto;

import java.util.HashMap;

public class CuposYTurnosEstacionesHorario {
    private HashMap<Long, HashMap<Long, Integer>> cupos = new HashMap<>();
    private HashMap<Long, HashMap<Long, Integer>> turnos = new HashMap<>();

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

    public void substractCupo(long estacionId, long horarioId){
        if(!this.cupos.containsKey(estacionId))
            this.putCupo(estacionId, horarioId, 0);
        
        int numCupos = this.getCupos(estacionId, horarioId);
        this.cupos.get(estacionId).put(horarioId, numCupos-1);
    }

    public void substractTurno(long estacionId, long horarioId){
        if(!this.turnos.containsKey(estacionId))
            this.putTurno(estacionId, horarioId, 0);
        
        int numTurnos = this.getTurnos(estacionId, horarioId);
        this.turnos.get(estacionId).put(horarioId, numTurnos-1);
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

    public HashMap<Long, HashMap<Long, Integer>> getCupos() {
        return cupos;
    }

    public HashMap<Long, HashMap<Long, Integer>> getTurnos() {
        return turnos;
    }

    
}
