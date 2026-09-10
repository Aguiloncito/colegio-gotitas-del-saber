/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.RegistroRequest;
import main.java.edu.ingsoft.colegio.gotitas.service.RegistroViewService;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;

/**
 * FXML Controller class
 *
 * @author AGUILON
 */
public class RegistroController implements Initializable {
    //atributos
    private final RegistroViewService registroService;
    private final SceneManager sceneManager;

    @FXML
    private TextField txtFieldUsuario;
    @FXML
    private TextField txtFieldPass;
    @FXML
    private ComboBox<String> comboRol;

    //Constructor
    public RegistroController(RegistroViewService registroService, SceneManager sceneManager) {
        this.registroService = registroService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboRol.setItems(FXCollections.observableArrayList("Docente", "Estudiante"));
    }

    //metodo para registrar
    public void handleRegistrar() throws Exception {
        String usuario = txtFieldUsuario.getText();
        String pass = txtFieldPass.getText();
        String rol = comboRol.getValue();

        if (usuario == null || usuario.isEmpty()
                || pass == null || pass.isEmpty()
                || rol == null || rol.isEmpty()) {
            sceneManager.showInfoAlert("Campos faltantes", "Revisar información", "Uno o mas campos están vacios", Alert.AlertType.CONFIRMATION);
        } else {
            try {
                RegistroRequest request = new RegistroRequest(usuario, pass, rol);
                registroService.registrar(request);
                sceneManager.showInfoAlert("Registro exitoso", "Usuario creado", "El usuario fue registrado correctamente", Alert.AlertType.INFORMATION);
                sceneManager.showLoginView();
            } catch (RuntimeException e) {
                sceneManager.showInfoAlert("Error al registrar", "Revisa tu información", e.getMessage(), Alert.AlertType.INFORMATION);
            }
        }
    }

    //metodo para regresar al login
    public void handleRegresar() throws Exception {
        sceneManager.showLoginView();
    }
}