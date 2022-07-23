package com.app.springapp.interfacesServicios;

import java.util.List;
import com.app.springapp.Exception.UsernameOrIdNotFound;
import com.app.springapp.dto.ChangePasswordForm;
import com.app.springapp.entity.Usuario;

/**
 * Servicio que permite realizar diferentes opciones relacionadas a la entidad
 * Usuario
 * 
 * @version July 2022
 */
public interface IUsuarioService {
    /**
     * @return Una lista con todos los usuarios almacenados
     */
    public List<Usuario> obtenerUsuarios();

    /**
     * Almacena una nueva instancia de la clase Usuario
     * 
     * @param usuario
     * @return El usuario que fue creado
     * @throws Exception se genera una excepción si el usuario ya existe
     */
    public Usuario crearUsuario(Usuario usuario) throws Exception;

    /**
     * @param id
     *           {@return el Usuario con el id especificado}
     * @throws UsernameOrIdNotFound se genera la excepción cuando no se encuentra el
     *                              usuario con el id especificado
     */
    public Usuario obtenerUsuarioPorId(Long id) throws UsernameOrIdNotFound;


    /**
     * Actualiza un Usuario almacenado reemplazándolo con el Usuario proporcionado
     * @param usuario Usuario con los nuevos datos para reemplazar
     * @return El Usuario actualizado
     * @throws UsernameOrIdNotFound se genera la excepción cuando no se encuentra el usuario a actualizar
     */
    public Usuario actualizarUsuario(Usuario usuario) throws UsernameOrIdNotFound;

    /**
     * Elimina un Usuario almacenado con el id especificado
     * @param id
     * @throws UsernameOrIdNotFound se genera la excepción cuando no se encuentra el usuario a eliminar
     */
    public void eliminarUsuario(Long id) throws UsernameOrIdNotFound;


    /**
     * Actualiza la contraseña de un Usuario
     * @deprecated
     * @param form Objeto con la nueva contraseña
     * @return El Usuario con la nueva contraseña
     * @throws Exception se genera la excepción cuando no se encuentra el usuario para actualizar su contraseña
     */
    public Usuario changePassword(ChangePasswordForm form) throws Exception;


    /**
     * @return El usuario que está actualmente loggeado en la aplicación
     * @throws Exception se genera la excepción si el usuario loggeado no coincide con un Usuario almacenado
     */
    public Usuario getLoggedUser() throws Exception;

    /**
     * Permite saber si el usuario logueado tiene un rol específico.
     * Se requiere ingresar el dato correspondiente a 'descripcion' del objeto Rol.
     * @param role Descripción del rol que se quiere consultar
     * @return True si el usuario logueado tiene el rol solicitado, False en otro caso
     */
    public boolean loggedUserHasRole(String role);

    /**
     * @return True si hay un usuario logueado, False en otro caso
     */
    public boolean hayUsuarioLogueado();
}
