package com.app.springapp.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.Exception.UsernameOrIdNotFound;
import com.app.springapp.dto.UsuarioDTO;
import com.app.springapp.repository.RolRepository;
import com.app.springapp.service.UsuarioService;

@Controller
public class UsuarioController {

    @Autowired
    UsuarioService serUsuario;

    @Autowired
    RolRepository repRole;

    @GetMapping({ "/", "/login" })
    public String index() {
        if(serUsuario.hayUsuarioLogueado())
            return "redirect:/inicio"; 
        return "index";
    }

    @PostMapping(value = "/user", consumes = "application/json")
    public ResponseEntity createUser(@RequestBody UsuarioDTO user) {
        try{
           user =  serUsuario.crearUsuario(user);
           return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch(CustomeFieldValidationException e){
            HashMap<String, String> response = new HashMap<String, String>();
            response.put("message", e.getMessage());

            return new ResponseEntity<HashMap<String,String>>(response ,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/user")
    public ResponseEntity listUsers() {
        try{
           List<UsuarioDTO> users =  serUsuario.obtenerUsuarios();
           return new ResponseEntity<List<UsuarioDTO>>(users, HttpStatus.OK);
        } catch(Exception e){
            HashMap<String, String> response = new HashMap<String, String>();
            response.put("message", e.getMessage());

            return new ResponseEntity<HashMap<String,String>>(response ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/user/{idUser}")
    public ResponseEntity listUserById(@PathVariable long idUser){
        try{
            UsuarioDTO user = serUsuario.obtenerUsuarioPorId(idUser);
            return new ResponseEntity<UsuarioDTO>(user, HttpStatus.OK);
        } catch(UsernameOrIdNotFound e){
            HashMap<String, String> response = new HashMap<String, String>();
            response.put("message", e.getMessage());
            return new ResponseEntity<HashMap<String,String>>(response ,HttpStatus.NOT_FOUND);

        } catch( Exception e){
            HashMap<String, String> response = new HashMap<String, String>();
            response.put("message", "Error al buscar usuario");
            return new ResponseEntity<HashMap<String,String>>(response ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    


}
    