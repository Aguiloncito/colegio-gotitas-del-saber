/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.dto.request.RegistroRequest;

/**
 *
 * @author PC
 */
public class RegistroRepository {

    //valida que el correo no este ya registrado en la tabla usuarios
    public boolean existsByEmail(String correoElectronico) throws SQLException {
        String sql = "SELECT id_usuario FROM usuarios WHERE email = ?";
        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase()
                .prepareStatement(sql)) {
            pstm.setString(1, correoElectronico);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        }
    }

    //valida que el id_docente exista en la tabla docentes (requisito de la FK fk_id_docente)
    public boolean existeDocente(String idDocente) throws SQLException {
        String sql = "SELECT id_docente FROM docentes WHERE id_docente = ?";
        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase()
                .prepareStatement(sql)) {
            pstm.setString(1, idDocente);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        }
    }

    //obtiene el id_rol real desde la tabla roles a partir del nombre mostrado en el combo (Docente/Estudiante)
    public Integer obtenerIdRolPorNombre(String nombreRol) throws SQLException {
        String sql = "SELECT id_rol FROM roles WHERE nombre_rol = ?";
        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase()
                .prepareStatement(sql)) {
            pstm.setString(1, nombreRol);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_rol");
                }
                return null;
            }
        }
    }

    //guarda el nuevo usuario respetando las columnas reales de la tabla usuarios
    public boolean save(RegistroRequest registroRequest, String contrasenaHasheada, int idRol) throws SQLException {
        String sql = "INSERT INTO usuarios (id_usuario, id_docente, contrasena_hash, id_rol, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = DataBaseConnection
                .getConnectionDataBase()
                .prepareStatement(sql)) {
            pstm.setString(1, UUID.randomUUID().toString());

            if (registroRequest.getIdDocente() == null || registroRequest.getIdDocente().isBlank()) {
                pstm.setNull(2, Types.VARCHAR);
            } else {
                pstm.setString(2, registroRequest.getIdDocente());
            }

            pstm.setString(3, contrasenaHasheada);
            pstm.setInt(4, idRol);
            pstm.setString(5, registroRequest.getCorreoElectronico());

            return pstm.executeUpdate() > 0;
        }
    }
}
