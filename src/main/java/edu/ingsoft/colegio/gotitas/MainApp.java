/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas;
  
import javafx.application.Application;
import javafx.stage.Stage;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;
import java.sql.SQLException;
/**
 *
 * @author AGUILON
 */
public class MainApp extends Application{
     
    private Stage primaryStage;
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        SceneManager sceneManager = new SceneManager(primaryStage);
        sceneManager.showLoginView();
        primaryStage.show();
    }
    
    public static void main(String[] args) throws Exception{
        launch();
    }
}
