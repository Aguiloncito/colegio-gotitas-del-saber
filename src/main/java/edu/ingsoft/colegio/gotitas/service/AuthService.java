/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.service;

import java.sql.SQLException;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.LoginRequest;
import main.java.edu.ingsoft.colegio.gotitas.dto.response.LoginResponse;
import main.java.edu.ingsoft.colegio.gotitas.repository.AuthRepository;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author AGUILON
 */
public class AuthService {
    private final AuthRepository authRepository;
    private boolean status = false;
    public AuthService(AuthRepository authRepository){
        this.authRepository = authRepository;
    }
public LoginResponse login (LoginRequest loginRequest) throws Exception{
    if ((loginRequest == null)) {
        throw new RuntimeException("Credenciales vacias.");
    }else if(loginRequest.getEmail() == null || loginRequest.getPassword()== null){
        throw new RuntimeException("El correo o la contraseña no pueden estar vacios");
    }
    LoginResponse response = authRepository.findUserByEmail(loginRequest);
    if (response== null) {
        throw new RuntimeException("Usuario no encontado");
    }
  String contrasenaHashed = response.getContraseña_Hash();
  if(contrasenaHashed == null){
      throw new RuntimeException("contrasena invalida.");
  }else{
      if (BCrypt.checkpw(loginRequest.getPassword(),contrasenaHashed )){
          return response;
      }
  }
      return null;
}
}

