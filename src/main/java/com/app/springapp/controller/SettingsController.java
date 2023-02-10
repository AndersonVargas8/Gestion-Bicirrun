package com.app.springapp.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.springapp.dto.EstacionDTO;
import com.app.springapp.service.EstacionService;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @Autowired
    EstacionService serEstacion;

    @GetMapping
    public String index(ModelMap model){
        model.addAttribute("estaciones", serEstacion.obtenerTodas());
        return "settings/index";
    }

    @PostMapping(value = "/estacionesHabilitadas", consumes = "application/json")
    public ResponseEntity setEstacionesHabilitadas(@RequestBody List<EstacionDTO> estaciones){
        try{
            serEstacion.updateEstacionesHabilitadas(estaciones);
            return new ResponseEntity(HttpStatus.OK);
        }catch(Exception e){
            HashMap<String, String> response = new HashMap<String, String>();
            response.put("message", e.getMessage());
            return new ResponseEntity<HashMap<String,String>>(response ,HttpStatus.BAD_REQUEST);      
        }
    }
}
