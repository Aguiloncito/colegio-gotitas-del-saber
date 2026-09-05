/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.RegistroRequest;

/**
 *
 * @author PC
 */
public class RegistroRepository {

    public boolean existsByNombreUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT nombre_usuario FROM usuarios WHERE nombre_usuario = ?";
        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase()
                .prepareStatement(sql)) {
            pstm.setString(1, nombreUsuario);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean save(RegistroRequest registroRequest, String contrasenaHasheada) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre_usuario, `contraseña_hash`, rol) VALUES (?, ?, ?)";
        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase()
                .prepareStatement(sql)) {
            pstm.setString(1, registroRequest.getNombreUsuario());
            pstm.setString(2, contrasenaHasheada);
            pstm.setString(3, registroRequest.getRol());
            return pstm.executeUpdate() > 0;
        }
    }
}