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
                || registroRequest.getNombreUsuario() == null || registroRequest.getNombreUsuario().isEmpty()
                || registroRequest.getContrasena() == null || registroRequest.getContrasena().isEmpty()
                || registroRequest.getRol() == null || registroRequest.getRol().isEmpty()) {
            throw new RuntimeException("El usuario, la contraseña y el rol no pueden estar vacios");
        }

        if (registroRepository.existsByNombreUsuario(registroRequest.getNombreUsuario())) {
            throw new RuntimeException("Ese nombre de usuario ya esta en uso");
        }

        String contrasenaHasheada = BCrypt.hashpw(registroRequest.getContrasena(), BCrypt.gensalt());
        boolean guardado = registroRepository.save(registroRequest, contrasenaHasheada);

        if (!guardado) {
            throw new RuntimeException("No se pudo registrar el usuario");
        }

        return new RegistroResponse(registroRequest.getNombreUsuario(), registroRequest.getRol());
    }
}
