/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.controller;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import main.java.edu.ingsoft.colegio.gotitas.service.AuthService;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;
import javafx.scene.control.Alert;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.LoginRequest;
import main.java.edu.ingsoft.colegio.gotitas.dto.response.LoginResponse;
/**
 * FXML Controller class
 *
 * @author AGUILON
 */
public class LoginController implements Initializable {
    //atributos
     private final AuthService authService;
     private final SceneManager sceneManager;
     @FXML
     private TextField txtFieldEmail;
     @FXML
     private TextField txtFieldPass;
    //Constructor
     public LoginController(AuthService authService, SceneManager sceneManager){
         this.authService = authService;
         this.sceneManager = sceneManager;
     }
             
             
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("TODO LO QUE ESTE ACA, SE EJECUTA CUANDO SE MUESTRA LA VISTA");
    } 
    
    //metodo
    public void handleLogin() throws Exception{
        if(txtFieldEmail.getText().isEmpty()||txtFieldPass.getText().isEmpty()){
            sceneManager.showInfoAlert("Campos faltantes", "Revisar información", "Uno o mas campos están vacios", Alert.AlertType.CONFIRMATION);
        }else{
            try{
                
            LoginResponse responseService = authService.login(new LoginRequest(txtFieldEmail.getText(), txtFieldPass.getText()));
            LoginResponse userLogged = new LoginResponse(responseService.getNombre(), responseService.getApellido());
            sceneManager.showInfoAlert("Bienvenido a gotitas del Saber", "Inicio exitoso", "Bienvenido: "+ userLogged.getNombre(), Alert.AlertType.INFORMATION);
            sceneManager.showDashBoardView();
            }catch(RuntimeException e){
                sceneManager.showInfoAlert("Datos incorrectos", "Revisa tu información", "Intenta de nuevo", Alert.AlertType.INFORMATION);
            }
        }
    }
     public void handleRegistrarse() throws Exception {
        sceneManager.showRegistroView();
    }

}
