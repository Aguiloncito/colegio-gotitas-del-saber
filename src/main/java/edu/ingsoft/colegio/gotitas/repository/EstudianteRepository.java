/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.ingsoft.colegio.gotitas.repository;

import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import main.java.edu.ingsoft.colegio.gotitas.config.DataBaseConnection;
import main.java.edu.ingsoft.colegio.gotitas.model.Estudiante;
/**
 *
 * @author AGUILON
 */
public class EstudianteRepository {
    
    public ObservableList<Estudiante> findAll(){
        String sql = "select\n" +
                  "  e.id_estudiante, \n"
                + "  e.nombre as nombre_estudiante, \n"
                + "  e.apellido as apellido_estudiante, \n"
                + "  e.correo_electronico,\n"
                + "  s.nombre_seccion,\n"
                + "  c.nombre_curso,\n"
                + "  d.nombre as nombre_docente, \n"
                + "  d.apellido as apellido_docente\n"
                + "from matriculas as m\n"
                + "inner join estudiantes as e\n"
                + "  on e.id_estudiante = m.id_estudiante\n"
                + "inner join asignacion_cursos as ac\n"
                + "  on ac.id_seccion = m.id_seccion\n"
                + "inner join secciones as s \n"
                + "  on s.id_seccion = ac.id_seccion\n"
                + "inner join cursos as c\n"
                + "  on c.id_curso = ac.id_curso\n"
                + "inner join docentes as d\n"
                + "  on d.id_docente = ac.id_docente;";
        try(PreparedStatement pstm = DataBaseConnection.getConnectionDataBase().prepareStatement(sql)){
            ResultSet rs = pstm.executeQuery();
            ObservableList<Estudiante> studentList = FXCollections.observableArrayList();
            while(rs.next()){
                studentList.add(new Estudiante(
                rs.getString("id_estudiante"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("correo_electronico"),
                rs.getString("nombre_seccion"),
                rs.getString("nombre_curso"),
                rs.getString("nombre"),
                rs.getString("apellido")
                ));
            }
            return studentList;
        }catch(SQLException e){
            throw new RuntimeException();
        }
    }
}
