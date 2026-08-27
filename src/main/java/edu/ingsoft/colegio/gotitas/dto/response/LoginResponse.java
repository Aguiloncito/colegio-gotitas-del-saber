/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.dto.response;

/**
 *  
 * @author AGUILON
 */
public class LoginResponse {
    private String nombre;
    private String apellido;
    private String contraseña_Hash;
    
    public LoginResponse(String nombre, String apellido, String contraseña_Hash) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.contraseña_Hash = contraseña_Hash;
    }
    
    //sobre carga
    public LoginResponse(String nombre, String apellido){
        this.nombre = nombre;
        this.apellido = apellido;
    }
    
    public String getNombre() {
        return nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public String getContraseña_Hash() {
        return contraseña_Hash;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public void setContraseña_Hash(String contraseña_Hash) {
        this.contraseña_Hash = contraseña_Hash;
    }
}