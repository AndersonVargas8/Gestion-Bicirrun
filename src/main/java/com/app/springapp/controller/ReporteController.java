package com.app.springapp.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.springapp.dto.ReporteTurnosEstudiante;
import com.app.springapp.service.TurnoService;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
    @Autowired
    TurnoService serTurno;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String index(ModelMap model){
        model.addAttribute(
            "reportes",
             this.reporteCumplidosIncumplidos("2000-01-01",     "2100-01-01").getBody()
        );
        return "reportes/reportes";
    }

    /**
     * @param initDateStr String in the format "yyyy-mm-dd" such as "2007-12-30"
     * @param finalDateStr String in the format "yyyy-mm-dd" such as "2007-12-30"
     */
    @GetMapping("/programados/{initDateStr}/{finalDateStr}")
    public ResponseEntity reporteProgramados(@PathVariable String initDateStr, @PathVariable String finalDateStr){
        HashMap<String, String> response = new HashMap<>();
        try{
            LocalDate initDate = LocalDate.parse(initDateStr);
            LocalDate finalDate = LocalDate.parse(finalDateStr);

            return new ResponseEntity<List<ReporteTurnosEstudiante>>(
                serTurno.getReporteProgramados(initDate, finalDate),
                HttpStatus.OK
            );
        } catch(DateTimeParseException e){
            response.put("message", "El formato de las fechas no es adecuado");
            return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param initDateStr String in the format "yyyy-mm-dd" such as "2007-12-30"
     * @param finalDateStr String in the format "yyyy-mm-dd" such as "2007-12-30"
     */
    @GetMapping("/programados/cumplidos/{initDateStr}/{finalDateStr}")
    public ResponseEntity reporteCumplidos(@PathVariable String initDateStr, @PathVariable String finalDateStr){
        HashMap<String, String> response = new HashMap<>();
        try{
            LocalDate initDate = LocalDate.parse(initDateStr);
            LocalDate finalDate = LocalDate.parse(finalDateStr);

            List<ReporteTurnosEstudiante> repProgramados = serTurno.getReporteProgramados(initDate, finalDate);
        
            return new ResponseEntity<List<ReporteTurnosEstudiante>>(
                serTurno.getReporteCumplidos(repProgramados, initDate, finalDate),
                HttpStatus.OK
            );
        } catch(DateTimeParseException e){
            response.put("message", "El formato de las fechas no es adecuado");
            return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param initDateStr String in the format "yyyy-mm-dd" such as "2007-12-30"
     * @param finalDateStr String in the format "yyyy-mm-dd" such as "2007-12-30"
     */
    @GetMapping("/programados/incumplidos/{initDateStr}/{finalDateStr}")
    public ResponseEntity reporteIncumplidos(@PathVariable String initDateStr, @PathVariable String finalDateStr){
        HashMap<String, String> response = new HashMap<>();
        try{
            LocalDate initDate = LocalDate.parse(initDateStr);
            LocalDate finalDate = LocalDate.parse(finalDateStr);

            List<ReporteTurnosEstudiante> repProgramados = serTurno.getReporteProgramados(initDate, finalDate);
        
            return new ResponseEntity<List<ReporteTurnosEstudiante>>(
                serTurno.getReporteIncumplidos(repProgramados, initDate, finalDate),
                HttpStatus.OK
            );
        } catch(DateTimeParseException e){
            response.put("message", "El formato de las fechas no es adecuado");
            return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param initDateStr String in the format "yyyy-mm-dd" such as "2007-12-30"
     * @param finalDateStr String in the format "yyyy-mm-dd" such as "2007-12-30"
     */
    @GetMapping("/programados/cumplidos/incumplidos/{initDateStr}/{finalDateStr}")
    public ResponseEntity reporteCumplidosIncumplidos(@PathVariable String initDateStr, @PathVariable String finalDateStr){
        HashMap<String, String> response = new HashMap<>();
        try{
            LocalDate initDate = LocalDate.parse(initDateStr);
            LocalDate finalDate = LocalDate.parse(finalDateStr);

            List<ReporteTurnosEstudiante> repProgramados = serTurno.getReporteProgramados(initDate, finalDate);
            repProgramados = serTurno.getReporteCumplidos(repProgramados, initDate, finalDate);
            
            return new ResponseEntity<List<ReporteTurnosEstudiante>>(
                serTurno.getReporteIncumplidos(repProgramados, initDate, finalDate),
                HttpStatus.OK
            );
        } catch(DateTimeParseException e){
            response.put("message", "El formato de las fechas no es adecuado");
            return new ResponseEntity<HashMap<String, String>>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
