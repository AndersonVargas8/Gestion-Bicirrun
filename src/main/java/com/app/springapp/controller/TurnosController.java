package com.app.springapp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.dto.TurnoDTO;
import com.app.springapp.dto.TurnosEstaciones;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Horario;
import com.app.springapp.interfacesServicios.IServicioEstacion;
import com.app.springapp.interfacesServicios.IServicioHorario;
import com.app.springapp.interfacesServicios.IServicioTurno;
import com.app.springapp.repository.EstadoTurnoRepository;
import com.app.springapp.repository.EstudianteRepository;
import com.app.springapp.service.CupoService;
import com.app.springapp.service.TurnoService;
import com.app.springapp.util.Mapper;

@Controller
@RequestMapping("/turnos")
public class TurnosController {
    @Autowired
    IServicioHorario serHorario;

    @Autowired
    IServicioEstacion serEstacion;

    @Autowired
    EstudianteRepository repEstudiante;

    @Autowired
    EstadoTurnoRepository repEstadoTurno;

    @Autowired
    CupoService serCupo;


    @Autowired
    IServicioTurno serTurno;


    @GetMapping
    public String index(ModelMap model) {
        model.addAttribute("horarios", serHorario.obtenerTodos());
        model.addAttribute("estudiantes", repEstudiante.findAll());
        model.addAttribute("calendario",serTurno.getCalendario(LocalDate.now().getMonthValue(), LocalDate.now().getYear()));
        return "turnos/turnos";
    }

    /**
     * Se construye un calendario para la fecha dada con los turnos programados
     * @param mes
     * @param anio
     * @param model
     * @return Fragmento que contiene el calendario renderizado
     */
    @GetMapping("/calendarioTurnos/{mes}/{anio}")
    public String calendarioTurnos(@PathVariable int mes, @PathVariable int anio, ModelMap model){
        model.addAttribute("calendario",serTurno.getCalendario(mes, anio));
        return "turnos/calendario::contenido-calendar";
    }

    
    /**
     * Se crea un nuevo turno con los parámetros especificados en el objeto turno.
     * @param turno Debe contener:
     * - fecha: String en formato "dd-mm-aaaa" (dd-mm-yyyy) ej: 31-12-1999
     * - observaciones (No obligatorio): String
     * - idEstacion: int
     * - idEstado (No obligatorio): int
     * - idEstudiante: int
     * - idHorario: int
     * @param model
     * @return 
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity crearTurno(@RequestBody TurnoDTO turnoDTO) {
        try{
             turnoDTO = serTurno.guardarTurno(turnoDTO);
        }catch(CustomeFieldValidationException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<TurnoDTO>(turnoDTO,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarTurno(@PathVariable int id){
        try{
            serTurno.eliminarTurno(id);
        }catch(CustomeFieldValidationException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity editarTurno(@PathVariable int id, @RequestBody TurnoDTO turnoDTO){
        try{
            turnoDTO = serTurno.editarTurno(id, turnoDTO);
        }catch(CustomeFieldValidationException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<TurnoDTO>(turnoDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity parcharTurno(@PathVariable int id, @RequestBody TurnoDTO turnoDTO){
        try{
            turnoDTO = serTurno.parcharTurno(id, turnoDTO);
        }catch(CustomeFieldValidationException e){
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<TurnoDTO>(turnoDTO, HttpStatus.OK);
    }

    
    @GetMapping("/diasDeshabilitados")
    @ResponseBody
    public TurnoService.DiasDeshabilitados getDiasDeshabilitados(){
        TurnoService.DiasDeshabilitados diasDeshabilitados = serTurno.obtenerDiasDeshabilitados();

        return diasDeshabilitados;
    }

    @GetMapping("/diasDeshabilitados/{idHorario}")
    @ResponseBody
    public TurnoService.DiasDeshabilitados getDiasDeshabilitadosPorHorario(@PathVariable int idHorario){
        TurnoService.DiasDeshabilitados diasDeshabilitados = serTurno.obtenerDiasDeshabilitados(idHorario);

        return diasDeshabilitados;
    }

    /**
     * 
     * @param fecha String en formato "dd-mm-aaaa"
     * @return Lista de horarios disponibles en la fecha dada
     */
    @GetMapping("/horariosDisponibles/{fecha}")
    @ResponseBody
    public List<Horario> getHorariosDisponibles(@PathVariable String fecha){
        if(Mapper.esFechaValida(fecha)){
            String[] fechaFormat = fecha.split("-");
            int dia = Integer.parseInt(fechaFormat[0]);
            int mes = Integer.parseInt(fechaFormat[1]);
            int anio = Integer.parseInt(fechaFormat[2]);
            List<Horario> horarios = serHorario.obtenerDisponiblesPorFecha(LocalDate.of(anio,mes,dia));
            return horarios;
        }else{
            throw new IllegalArgumentException("La fecha no está en el formato correcto. Debe ser 'dd-mm-aaaa'");
        }
    }

    /**
     * 
     * @param fecha String en formato "dd-mm-aaaa"
     * @return Lista de estaciones disponibles en la fecha y horario dados
     */
    @GetMapping("/estacionesDisponibles/{fecha}/{idHorario}")
    @ResponseBody
    public List<Estacion> getEstacionesDisponibles(@PathVariable String fecha, @PathVariable int idHorario){
        LocalDate fechaLista = null;
        Horario horario = null;
        if(Mapper.esFechaValida(fecha)){
            String[] fechaFormat = fecha.split("-");
            int dia = Integer.parseInt(fechaFormat[0]);
            int mes = Integer.parseInt(fechaFormat[1]);
            int anio = Integer.parseInt(fechaFormat[2]);
            fechaLista = LocalDate.of(anio,mes,dia);
        }else{
            throw new IllegalArgumentException("La fecha no está en el formato correcto. Debe ser 'dd-mm-aaaa'");
        }
        try{
            horario = serHorario.buscarPorId(idHorario);
        }catch(Exception e){
            throw new IllegalArgumentException("No se encontró el horario con el id proporcionado");
        }

        List<Estacion> estaciones = serEstacion.obtenerDisponiblesPorFechaYHorario(fechaLista,horario);
        return estaciones;
    }

    @GetMapping("/dia/{fecha}")
    @ResponseBody
    public TurnosEstaciones getTurnosEstaciones(@PathVariable String fecha){
        LocalDate fechaLista = null;
        if(!Mapper.esFechaValida(fecha)){
            throw new IllegalArgumentException("La fecha no está en el formato correcto. Debe ser 'dd-mm-aaaa'");
        }else{
            String[] fechaFormat = fecha.split("-");
            int dia = Integer.parseInt(fechaFormat[0]);
            int mes = Integer.parseInt(fechaFormat[1]);
            int anio = Integer.parseInt(fechaFormat[2]);
            fechaLista = LocalDate.of(anio,mes,dia);
        }

        TurnosEstaciones turnos =  serTurno.obtenerTurnosEstaciones(fechaLista);
        return turnos;
        
    }
 
}
