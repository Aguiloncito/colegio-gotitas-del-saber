package main.java.edu.ingsoft.colegio.gotitas.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.model.Docente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DocenteRepository {

    public Docente findById(String idDocente) throws SQLException {
        String sql = "SELECT id_docente, nombre, apellido, correo_electronico FROM docentes WHERE id_docente = ?";
        try (Connection conn = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, idDocente);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Docente(
                            rs.getString("id_docente"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("correo_electronico")
                    );
                }
            }
        }
        return null;
    }

    public ObservableList<DocenteEstudianteDTO> findEstudiantesByDocente(String idDocente) throws SQLException {
        ObservableList<DocenteEstudianteDTO> lista = FXCollections.observableArrayList();
        
        String sql = "SELECT " +
                     "  e.id_estudiante, " +
                     "  e.nombre AS nombre_estudiante, " +
                     "  e.apellido AS apellido_estudiante, " +
                     "  e.correo_electronico, " +
                     "  s.nombre_seccion, " +
                     "  c.nombre_curso, " +
                     "  m.nota_tareas, " +
                     "  m.nota_examenes, " +
                     "  m.nota_final " +
                     "FROM asignacion_cursos ac " +
                     "INNER JOIN secciones s ON s.id_seccion = ac.id_seccion " +
                     "INNER JOIN cursos c ON c.id_curso = ac.id_curso " +
                     "INNER JOIN matriculas m ON m.id_seccion = ac.id_seccion " +
                     "INNER JOIN estudiantes e ON e.id_estudiante = m.id_estudiante " +
                     "WHERE ac.id_docente = ?";

        try (Connection conn = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            
            pstm.setString(1, idDocente);
            
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    lista.add(new DocenteEstudianteDTO(
                            rs.getString("id_estudiante"),
                            rs.getString("nombre_estudiante"),
                            rs.getString("apellido_estudiante"),
                            rs.getString("correo_electronico"),
                            rs.getString("nombre_seccion"),
                            rs.getString("nombre_curso"),
                            rs.getDouble("nota_tareas"),
                            rs.getDouble("nota_examenes"),
                            rs.getDouble("nota_final")
                    ));
                }
            }
        }
        return lista;
    }

    public boolean guardarOAsignarEstudiante(String idEstudiante, String nombre, String apellido, String idDocente) throws SQLException {
        String sqlSeccion = "SELECT id_seccion FROM asignacion_cursos WHERE id_docente = ? LIMIT 1";
        String idSeccion = null;

        try (Connection conn = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pstm = conn.prepareStatement(sqlSeccion)) {
            pstm.setString(1, idDocente);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    idSeccion = rs.getString("id_seccion");
                }
            }
        }

        if (idSeccion == null) {
            throw new SQLException("El docente no tiene una sección/curso asignado en la BD.");
        }

        // 1. Insertar estudiante en la tabla 'estudiantes' si no existe
        String sqlEstudiante = "INSERT INTO estudiantes (id_estudiante, nombre, apellido) VALUES (?, ?, ?) " +
                               "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), apellido = VALUES(apellido)";
        try (Connection conn = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pstm = conn.prepareStatement(sqlEstudiante)) {
            pstm.setString(1, idEstudiante);
            pstm.setString(2, nombre);
            pstm.setString(3, apellido);
            pstm.executeUpdate();
        }

        // 2. Matricular estudiante en la sección
        String sqlMatricula = "INSERT INTO matriculas (id_estudiante, id_seccion, nota_tareas, nota_examenes, nota_final) " +
                              "VALUES (?, ?, 0.0, 0.0, 0.0) " +
                              "ON DUPLICATE KEY UPDATE id_seccion = VALUES(id_seccion)";
        try (Connection conn = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pstm = conn.prepareStatement(sqlMatricula)) {
            pstm.setString(1, idEstudiante);
            pstm.setString(2, idSeccion);
            return pstm.executeUpdate() > 0;
        }
    }

    public boolean guardarOActualizarNotas(String idEstudiante, double tareas, double examenes, double notaFinal) throws SQLException {
        String sql = "UPDATE matriculas SET nota_tareas = ?, nota_examenes = ?, nota_final = ? WHERE id_estudiante = ?";
        try (Connection conn = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setDouble(1, tareas);
            pstm.setDouble(2, examenes);
            pstm.setDouble(3, notaFinal);
            pstm.setString(4, idEstudiante);
            return pstm.executeUpdate() > 0;
        }
    }

    public boolean eliminarEstudiante(String idEstudiante) throws SQLException {
        String sqlMatricula = "DELETE FROM matriculas WHERE id_estudiante = ?";
        try (Connection conn = DataBaseConnection.getConnectionDataBase();
             PreparedStatement pstm = conn.prepareStatement(sqlMatricula)) {
            pstm.setString(1, idEstudiante);
            return pstm.executeUpdate() > 0;
        }
    }

    public static class DocenteEstudianteDTO {
        private String idEstudiante;
        private String nombre;
        private String apellido;
        private String correo;
        private String seccion;
        private String curso;
        private Double notaTareas;
        private Double notaExamenes;
        private Double notaFinal;

        public DocenteEstudianteDTO(String idEstudiante, String nombre, String apellido, String correo, String seccion, String curso, Double notaTareas, Double notaExamenes, Double notaFinal) {
            this.idEstudiante = idEstudiante;
            this.nombre = nombre;
            this.apellido = apellido;
            this.correo = correo;
            this.seccion = seccion;
            this.curso = curso;
            this.notaTareas = notaTareas;
            this.notaExamenes = notaExamenes;
            this.notaFinal = notaFinal;
        }

        public String getIdEstudiante() { return idEstudiante; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getCorreo() { return correo; }
        public String getSeccion() { return seccion; }
        public String getCurso() { return curso; }
        public Double getNotaTareas() { return notaTareas; }
        public void setNotaTareas(Double notaTareas) { this.notaTareas = notaTareas; }
        public Double getNotaExamenes() { return notaExamenes; }
        public void setNotaExamenes(Double notaExamenes) { this.notaExamenes = notaExamenes; }
        public Double getNotaFinal() { return notaFinal; }
        public void setNotaFinal(Double notaFinal) { this.notaFinal = notaFinal; }
    }
}