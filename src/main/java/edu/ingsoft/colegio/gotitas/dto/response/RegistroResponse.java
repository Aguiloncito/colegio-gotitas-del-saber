/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.dto.response;

/**
 *
 * @author PC
 */
public class RegistroResponse {

    private final String correoElectronico;
    private final String rol;

    public RegistroResponse(String correoElectronico, String rol) {
        this.correoElectronico = correoElectronico;
        this.rol = rol;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getRol() {
        return rol;
    }
}
