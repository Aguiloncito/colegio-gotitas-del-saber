/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.RegistroRequest;
import main.java.edu.ingsoft.colegio.gotitas.dto.response.RegistroResponse;
import main.java.edu.ingsoft.colegio.gotitas.service.RegistroViewService;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;

/**
 *
 * @author PC
 */
public class RegistroController {
    private final RegistroViewService registroService;
    private final SceneManager sceneManager;

    @FXML
    private TextField txtFieldUsuario;
    @FXML
    private PasswordField txtFieldPass;
    @FXML
    private ComboBox<String> comboRol;

    public RegistroController(RegistroViewService registroService, SceneManager sceneManager) {
        this.registroService = registroService;
        this.sceneManager = sceneManager;
    }


    public void initialize(URL url, ResourceBundle rb) {
        // TODO: reemplaza estos valores por los roles reales de tu
        // aplicacion (el ComboBox se dejo editable, asi que tambien
        // se puede escribir un rol que no este en esta lista).
        comboRol.getItems().addAll("ADMIN", "OPERADOR");
    }

    public void handleRegistro() {
        String rol = comboRol.getEditor().getText();

        if (txtFieldUsuario.getText().isEmpty() || txtFieldPass.getText().isEmpty() || rol.isEmpty()) {
            sceneManager.showInfoAlert("Campos faltantes", "Revisar informacion", "Uno o mas campos estan vacios", Alert.AlertType.CONFIRMATION);
            return;
        }

        try {
            RegistroResponse registrado = registroService.registrar(
                    new RegistroRequest(txtFieldUsuario.getText(), txtFieldPass.getText(), rol)
            );
            sceneManager.showInfoAlert("Registro exitoso", "Usuario creado", "Bienvenido, " + registrado.getNombreUsuario(), Alert.AlertType.INFORMATION);
            sceneManager.showLoginView();
        } catch (RuntimeException e) {
            sceneManager.showInfoAlert("No se pudo registrar", "Revisa tu informacion", e.getMessage(), Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", "Error inesperado", e.getMessage(), Alert.AlertType.INFORMATION);
        }
    }

    public void handleRegresar() throws Exception {
        sceneManager.showLoginView();
    }
}
