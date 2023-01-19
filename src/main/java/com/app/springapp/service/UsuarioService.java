package com.app.springapp.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.springapp.Exception.CustomeFieldValidationException;
import com.app.springapp.Exception.UsernameOrIdNotFound;
import com.app.springapp.dto.UsuarioDTO;
import com.app.springapp.entity.Estacion;
import com.app.springapp.entity.Rol;
import com.app.springapp.entity.Usuario;
import com.app.springapp.repository.EstacionRepository;
import com.app.springapp.repository.RolRepository;
import com.app.springapp.repository.UsuarioRepository;
import com.app.springapp.util.Mapper;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repUser;

    @Autowired
    RolRepository repRol;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EstacionRepository repEstacion;

    public List<UsuarioDTO> obtenerUsuarios() {
        Iterable<Usuario> users = repUser.findAll();

        List<UsuarioDTO> usersDto = new ArrayList<>();

        for (Usuario user : users) {
            usersDto.add(Mapper.mapToUsuarioDto(user));
        }

        return usersDto;
    }

    public UsuarioDTO obtenerUsuarioPorId(Long id) throws UsernameOrIdNotFound {
        Usuario user = repUser.findById(id).orElseThrow(() -> new UsernameOrIdNotFound("El Id del usuario no existe"));
        return Mapper.mapToUsuarioDto(user);
    }

    public UsuarioDTO crearUsuario(UsuarioDTO userDto) throws CustomeFieldValidationException {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword()))
            throw new CustomeFieldValidationException("Las contraseñas no coinciden");

        String encodePassword = bCryptPasswordEncoder.encode(userDto.getPassword());
        Rol role = repRol.findByNombre("ESTACION");

        Usuario user = new Usuario();
        user.setUsername(userDto.getUsername());
        user.setPassword(encodePassword);
        user.setRol(role);

        for (Long estacionId : userDto.getEstaciones()) {
            Optional<Estacion> estacion = repEstacion.findById(estacionId);

            if (estacion.isPresent()) {
                user.addEstacion(estacion.get());
            }
        }

        try {
            user = repUser.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new CustomeFieldValidationException("El usuario ingresado ya existe");
        } catch (Exception e) {
            throw new CustomeFieldValidationException("Error al guardar el usuario");
        }

        return Mapper.mapToUsuarioDto(user);
    }

    // @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public UsuarioDTO actualizarUsuario(UsuarioDTO userDto) throws UsernameOrIdNotFound, CustomeFieldValidationException, Exception {
        Usuario user = repUser.findById((long) userDto.getId())
                .orElseThrow(() -> new UsernameOrIdNotFound("El Id del usuario no existe"));

            userDto.setId(null);
        Field[] fields = userDto.getClass().getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();
            fieldName = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);

            Method getMethod = userDto.getClass().getMethod("get" + fieldName);
            if (getMethod.invoke(userDto) != null) {
                if (getMethod.invoke(userDto) instanceof List) {
                    if(fieldName.equals("Estaciones")){
                        user.setEstaciones(null);
                        ArrayList<Long> estacionesId = userDto.getEstaciones();
                        for (Long idEstacion: estacionesId){
                            Estacion estacion = repEstacion.findById(idEstacion)
                            .orElseThrow(() -> new UsernameOrIdNotFound("El id de estación no existe"));

                            user.addEstacion(estacion);
                        }
                    }
                }else{
                    Method setMethod = null;

                    try{ 
                        setMethod = user.getClass().getMethod("set" + fieldName, getMethod.invoke(userDto).getClass());
                    }catch(Exception e){
                        continue;
                    }

                    if(fieldName.equals("Password")){
                        if(!userDto.getPassword().equals(userDto.getConfirmPassword()))
                            throw new CustomeFieldValidationException("Las contraseñas no coinciden");
                        
                        String encode = bCryptPasswordEncoder.encode(userDto.getPassword());
                        userDto.setPassword(encode);
                    }
                    setMethod.invoke(user, getMethod.invoke(userDto));
                }
            }
        }

        
        user = repUser.save(user);

        return Mapper.mapToUsuarioDto(user);
    }

    /*
     * @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
     * public void eliminarUsuario(Long id) throws UsernameOrIdNotFound {
     * Usuario user = obtenerUsuarioPorId(id);
     * 
     * repUser.delete(user);
     * }
     */

    public boolean isLoggedUserADMIN() {
        return loggedUserHasRole("ROLE_ADMIN");
    }

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

    /*
     * public Usuario changePassword(ChangePasswordForm form) throws Exception {
     * Usuario storedUser = obtenerUsuarioPorId(form.getId());
     * if (!isLoggedUserADMIN() &&
     * !passwordEncoder.matches(form.getCurrentPassword(),
     * storedUser.getPassword())) {
     * throw new Exception("Contraseña actual incorrecta.");
     * }
     * 
     * if (!isLoggedUserADMIN() &&
     * form.getCurrentPassword().equals(form.getNewPassword())) {
     * throw new
     * Exception("La nueva contraseña debe ser diferente a la actual contraseña");
     * }
     * 
     * if (!form.getNewPassword().equals(form.getConfirmPassword())) {
     * throw new Exception("Las contraseñas no coinciden");
     * }
     * 
     * String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());
     * storedUser.setPassword(encodePassword);
     * return repUser.save(storedUser);
     * }
     */

    public Usuario getLoggedUser() throws Exception {
        // Obtener el usuario logeado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserDetails loggedUser = null;

        // Verificar que ese objeto traido de sesion es el usuario
        if (principal instanceof UserDetails) {
            loggedUser = (UserDetails) principal;
        }

        Usuario myUser = repUser
                .findByUsername(loggedUser.getUsername())
                .orElseThrow(() -> new Exception("Ocurrió un problema al obtener el usuario de la sesión"));

        return myUser;
    }

    public boolean hayUsuarioLogueado() {
        Usuario usuario;
        try {
            usuario = getLoggedUser();
            if (usuario != null)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
