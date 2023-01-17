package com.app.springapp.dto;

public class CountCuposDTO{
    public Long horarioId;
    public int sumCupos;

    public CountCuposDTO(long horarioId, long sumCupos) {
        this.horarioId = horarioId;
        this.sumCupos = (int)sumCupos;
    }

    
}
