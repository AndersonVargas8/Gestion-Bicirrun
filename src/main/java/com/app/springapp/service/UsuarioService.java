package com.app.springapp.service;

import java.util.List;
import java.util.Optional;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.Exception.UsernameOrIdNotFound;
import com.app.springapp.dto.ChangePasswordForm;
import com.app.springapp.entity.Usuario;
import com.app.springapp.interfacesServicios.IUsuarioService;
import com.app.springapp.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    UsuarioRepository repUser;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> obtenerUsuarios() {
        return (List<Usuario>)repUser.findAll();
    }

    private boolean checkUserNameAvailable(Usuario user) throws Exception {
        Optional<Usuario> userFound = repUser.findByUsername(user.getUsername());

        if (userFound.isPresent()) {
            throw new CustomeFieldValidationException("Nombre de usuario no disponible","username");
        }
        return true;
    }

    private boolean checkPasswordValid(Usuario user) throws Exception {
        if (user.getPassword().equals(" ")) {
            throw new Exception("La contraseña está vacía");
        }
        return true;
    }

    @Override
    public Usuario crearUsuario(Usuario user) throws Exception {
        if (checkUserNameAvailable(user) && checkPasswordValid(user)) {
            String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encodePassword);
            user = repUser.save(user);
        }

        return user;
    }

    @Override
    public Usuario obtenerUsuarioPorId(Long id) throws UsernameOrIdNotFound {
        Usuario user = repUser.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("El Id del usuario no existe"));
        return user;
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public Usuario actualizarUsuario(Usuario fromUser) throws UsernameOrIdNotFound{
        Usuario toUser = obtenerUsuarioPorId(fromUser.getId());
        mapUser(fromUser, toUser);
        Usuario user = repUser.save(toUser);
        return user;
    }

    protected void mapUser(Usuario from, Usuario to) {
        to.setUsername(from.getUsername());
        to.setNombre(from.getNombre());
        to.setApellido(from.getApellido());
        to.setRol(from.getRol());
    }

    @Override
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void eliminarUsuario(Long id) throws UsernameOrIdNotFound {
        Usuario user = obtenerUsuarioPorId(id);

        repUser.delete(user);
    }

    public boolean isLoggedUserADMIN() {
        return loggedUserHasRole("ROLE_ADMIN");
    }

    @Override
    public boolean loggedUserHasRole(String role) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails loggedUser = null;
        Object roles = null;
        if (principal instanceof UserDetails) {
            loggedUser = (UserDetails) principal;

            roles = loggedUser.getAuthorities().stream()
                    .filter(x -> role.equals(x.getAuthority()))
                    .findFirst().orElse(null); // loggedUser = null;
        }
        return roles != null ? true : false;
    }

    @Override
    public Usuario changePassword(ChangePasswordForm form) throws Exception {
        Usuario storedUser = obtenerUsuarioPorId(form.getId());
        if (!isLoggedUserADMIN() && !passwordEncoder.matches(form.getCurrentPassword(), storedUser.getPassword())) {
            throw new Exception("Contraseña actual incorrecta.");
        }

        if (!isLoggedUserADMIN() && form.getCurrentPassword().equals(form.getNewPassword())) {
            throw new Exception("La nueva contraseña debe ser diferente a la actual contraseña");
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            throw new Exception("Las contraseñas no coinciden");
        }

        String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());
        storedUser.setPassword(encodePassword);
        return repUser.save(storedUser);
    }
    
    @Override
    public Usuario getLoggedUser() throws Exception {
        //Obtener el usuario logeado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        UserDetails loggedUser = null;
    
        //Verificar que ese objeto traido de sesion es el usuario
        if (principal instanceof UserDetails) {
            loggedUser = (UserDetails) principal;
        }
        
        Usuario myUser = repUser
                .findByUsername(loggedUser.getUsername()).orElseThrow(() -> new Exception("Ocurrió un problema al obtener el usuario de la sesión"));
        
        return myUser;
    }

    @Override
    public boolean hayUsuarioLogueado(){
        Usuario usuario;
        try{
            usuario = getLoggedUser();
            if(usuario != null)
                return true;
        }catch(Exception e){
            return false;
        }
        return false;
    }
}
