package main.java.edu.ingsoft.colegio.gotitas.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.ingsoft.colegio.gotitas.model.Docente;
import main.java.edu.ingsoft.colegio.gotitas.repository.DocenteRepository;
import main.java.edu.ingsoft.colegio.gotitas.repository.DocenteRepository.DocenteEstudianteDTO;
import main.java.edu.ingsoft.colegio.gotitas.util.SceneManager;

import java.sql.SQLException;

public class DocenteController {

    @FXML private Label lblDocenteNombre;
    @FXML private Label lblDocenteCorreo;

    @FXML private TableView<DocenteEstudianteDTO> tblEstudiantes;
    @FXML private TableColumn<DocenteEstudianteDTO, String> colId;
    @FXML private TableColumn<DocenteEstudianteDTO, String> colNombre;
    @FXML private TableColumn<DocenteEstudianteDTO, String> colApellido;
    @FXML private TableColumn<DocenteEstudianteDTO, String> colCurso;
    @FXML private TableColumn<DocenteEstudianteDTO, String> colSeccion;
    @FXML private TableColumn<DocenteEstudianteDTO, Double> colTareas;
    @FXML private TableColumn<DocenteEstudianteDTO, Double> colExamenes;
    @FXML private TableColumn<DocenteEstudianteDTO, Double> colNotaFinal;

    @FXML private TextField txtIdEstudiante;
    @FXML private TextField txtNombreEstudiante;
    @FXML private TextField txtNotaTareas;
    @FXML private TextField txtNotaExamenes;

    private final DocenteRepository docenteRepository;
    private final SceneManager sceneManager;
    private ObservableList<DocenteEstudianteDTO> listaEstudiantes;
    private String currentDocenteId;

    public DocenteController(DocenteRepository docenteRepository, SceneManager sceneManager) {
        this.docenteRepository = docenteRepository;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        configurarTabla();

        tblEstudiantes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                seleccionarEstudiante(newSelection);
            }
        });
    }

    public void initData(String idDocente) {
        this.currentDocenteId = idDocente;
        cargarDatosDocente(idDocente);
        cargarEstudiantes(idDocente);
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idEstudiante"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        colSeccion.setCellValueFactory(new PropertyValueFactory<>("seccion"));
        colTareas.setCellValueFactory(new PropertyValueFactory<>("notaTareas"));
        colExamenes.setCellValueFactory(new PropertyValueFactory<>("notaExamenes"));
        colNotaFinal.setCellValueFactory(new PropertyValueFactory<>("notaFinal"));
    }

    public void cargarDatosDocente(String idDocente) {
        try {
            Docente docente = docenteRepository.findById(idDocente);
            if (docente != null) {
                lblDocenteNombre.setText(docente.getNombreDocente() + " " + docente.getApellidoDocente());
                lblDocenteCorreo.setText(docente.getCorreoElectronico());
            }
        } catch (SQLException e) {
            sceneManager.showInfoAlert("Error", "Error BD", "No se cargaron datos del docente.", Alert.AlertType.ERROR);
        }
    }

    private void cargarEstudiantes(String idDocente) {
        try {
            listaEstudiantes = docenteRepository.findEstudiantesByDocente(idDocente);
            tblEstudiantes.setItems(listaEstudiantes);
        } catch (SQLException e) {
            sceneManager.showInfoAlert("Error", "Error de Consulta", "Error al cargar la lista: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void seleccionarEstudiante(DocenteEstudianteDTO estudiante) {
        txtIdEstudiante.setText(estudiante.getIdEstudiante());
        txtNombreEstudiante.setText(estudiante.getNombre() + " " + estudiante.getApellido());
        txtNotaTareas.setText(String.valueOf(estudiante.getNotaTareas()));
        txtNotaExamenes.setText(String.valueOf(estudiante.getNotaExamenes()));
    }

    @FXML
    private void handleGuardarEstudiante() {
        String id = txtIdEstudiante.getText().trim();
        String nombreCompleto = txtNombreEstudiante.getText().trim();

        if (id.isEmpty() || nombreCompleto.isEmpty()) {
            sceneManager.showInfoAlert("Atención", "Campos incompletos", "Ingrese el ID y Nombre Completo.", Alert.AlertType.WARNING);
            return;
        }

        String[] partes = nombreCompleto.split(" ", 2);
        String nombre = partes[0];
        String apellido = partes.length > 1 ? partes[1] : "";

        try {
            boolean exito = docenteRepository.guardarOAsignarEstudiante(id, nombre, apellido, currentDocenteId);
            if (exito) {
                sceneManager.showInfoAlert("Éxito", "Estudiante Guardado", "Estudiante asignado correctamente.", Alert.AlertType.INFORMATION);
                cargarEstudiantes(currentDocenteId);
                limpiarCampos();
            }
        } catch (SQLException e) {
            sceneManager.showInfoAlert("Error", "Error de Guardado", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleGuardarNota() {
        DocenteEstudianteDTO seleccionado = tblEstudiantes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showInfoAlert("Aviso", "Sin selección", "Seleccione un estudiante de la lista.", Alert.AlertType.WARNING);
            return;
        }

        try {
            double tareas = Double.parseDouble(txtNotaTareas.getText());
            double examenes = Double.parseDouble(txtNotaExamenes.getText());
            double notaFinal = (tareas + examenes) / 2.0;

            boolean exito = docenteRepository.guardarOActualizarNotas(seleccionado.getIdEstudiante(), tareas, examenes, notaFinal);
            if (exito) {
                seleccionado.setNotaTareas(tareas);
                seleccionado.setNotaExamenes(examenes);
                seleccionado.setNotaFinal(notaFinal);
                tblEstudiantes.refresh();
                sceneManager.showInfoAlert("Éxito", "Guardado", "Notas guardadas en la BD.", Alert.AlertType.INFORMATION);
            }
        } catch (NumberFormatException e) {
            sceneManager.showInfoAlert("Error", "Formato Inválido", "Ingrese números válidos.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            sceneManager.showInfoAlert("Error", "Error BD", "No se pudo actualizar la nota.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleActualizarNota() {
        handleGuardarNota();
    }

    @FXML
    private void handleEliminarEstudiante() {
        DocenteEstudianteDTO seleccionado = tblEstudiantes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            sceneManager.showInfoAlert("Aviso", "Sin selección", "Seleccione un estudiante para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean exito = docenteRepository.eliminarEstudiante(seleccionado.getIdEstudiante());
            if (exito) {
                listaEstudiantes.remove(seleccionado);
                limpiarCampos();
                sceneManager.showInfoAlert("Éxito", "Eliminado", "Estudiante eliminado correctamente.", Alert.AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            sceneManager.showInfoAlert("Error", "Error BD", "No se pudo eliminar el estudiante.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleRegresar(){
        sceneManager.showLoginView();
    }

    @FXML
    private void handleLimpiar() {
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtIdEstudiante.clear();
        txtNombreEstudiante.clear();
        txtNotaTareas.clear();
        txtNotaExamenes.clear();
        tblEstudiantes.getSelectionModel().clearSelection();
    }
}