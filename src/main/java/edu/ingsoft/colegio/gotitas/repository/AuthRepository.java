/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import java.sql.SQLException;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.LoginRequest;
import main.java.edu.ingsoft.colegio.gotitas.dto.response.LoginResponse;

/**
 *
 * @author AGUILON
 */
public class AuthRepository {
    //atributos
    private boolean sqlStatus = false;
    //constructor
    public LoginResponse findUserByEmail(LoginRequest loginRequest)throws SQLException{
        String sql = "select d.nombre, d.apellido, u.contraseña_hash from usuarios as u" +
            " right join docentes as d" +
            " on d.id_docente = u.id_docente" +
            " where email = ? ";
        try(PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)){
            pstm.setString(1, loginRequest.getEmail());
            ResultSet rs = pstm.executeQuery();
            if(rs.next()){
            return new LoginResponse(rs.getString("nombre"),rs.getString("apellido"),rs.getString("contraseña_hash"));
            }
        }catch(SQLException e){
            System.out.println("Error al encontrar el EMAIL " + e.getMessage());
        }
        return null;
        }
}
