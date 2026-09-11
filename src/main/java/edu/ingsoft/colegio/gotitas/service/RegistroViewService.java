/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.service;

import main.java.edu.ingsoft.colegio.gotitas.dto.request.RegistroRequest;
import main.java.edu.ingsoft.colegio.gotitas.dto.response.RegistroResponse;
import main.java.edu.ingsoft.colegio.gotitas.repository.RegistroRepository;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author PC
 */
public class RegistroViewService {

    private final RegistroRepository registroRepository;

    public RegistroViewService(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    public RegistroResponse registrar(RegistroRequest registroRequest) throws Exception {
        if (registroRequest == null
                || registroRequest.getCorreoElectronico() == null || registroRequest.getCorreoElectronico().isEmpty()
                || registroRequest.getContrasena() == null || registroRequest.getContrasena().isEmpty()
                || registroRequest.getRol() == null || registroRequest.getRol().isEmpty()) {
            throw new RuntimeException("El correo, la contraseña y el rol no pueden estar vacios");
        }

        //el id_docente solo es obligatorio cuando el rol seleccionado es Docente
        if ("Docente".equalsIgnoreCase(registroRequest.getRol())
                && (registroRequest.getIdDocente() == null || registroRequest.getIdDocente().isBlank())) {
            throw new RuntimeException("El ID de docente es obligatorio para el rol Docente");
        }

        if (registroRepository.existsByEmail(registroRequest.getCorreoElectronico())) {
            throw new RuntimeException("Ese correo ya esta registrado");
        }

        //si mandaron id_docente, validamos que exista en la tabla docentes (evita violar la FK fk_id_docente)
        if (registroRequest.getIdDocente() != null && !registroRequest.getIdDocente().isBlank()
                && !registroRepository.existeDocente(registroRequest.getIdDocente())) {
            throw new RuntimeException("El ID de docente no existe");
        }

        Integer idRol = registroRepository.obtenerIdRolPorNombre(registroRequest.getRol());
        if (idRol == null) {
            throw new RuntimeException("El rol seleccionado no existe");
        }

        String contrasenaHasheada = BCrypt.hashpw(registroRequest.getContrasena(), BCrypt.gensalt(12));
        boolean guardado = registroRepository.save(registroRequest, contrasenaHasheada, idRol);

        if (!guardado) {
            throw new RuntimeException("No se pudo registrar el usuario");
        }

        return new RegistroResponse(registroRequest.getCorreoElectronico(), registroRequest.getRol());
    }
}
