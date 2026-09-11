/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.dto.request;

/**
 *
 * @author PC
 */
public class RegistroRequest {
    private final String correoElectronico;
    private final String contrasena;
    private final String rol;
    private final String idDocente;

    public RegistroRequest(String correoElectronico, String contrasena, String rol, String idDocente) {
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.rol = rol;
        this.idDocente = idDocente;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getRol() {
        return rol;
    }

    public String getIdDocente() {
        return idDocente;
    }
}
