package Proyecto;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

import java.io.*;
import java.util.ArrayList;

public class EcoIslaController {

    @FXML
    private ComboBox<String> comboEmbarcacion;

    @FXML
    private ComboBox<String> comboHorario;

    @FXML
    private GridPane gridAsientos;

    @FXML
    private Label lblAsientosSeleccionados;

    @FXML
    private Label lblTotal;

    private Embarcacion[] embarcaciones;

    private Button[][] botones;

    private ArrayList<String> asientosSeleccionados = new ArrayList<>();

    private final int VALOR_PASAJE = 15000;

    @FXML
    public void initialize() {
        embarcaciones = new Embarcacion[3];
        embarcaciones[0] = new Embarcacion(1, "EcoIsla I");
        embarcaciones[1] = new Embarcacion(2, "EcoIsla II");
        embarcaciones[2] = new Embarcacion(3, "EcoIsla III");

        for (Embarcacion e : embarcaciones) {
            comboEmbarcacion.getItems().add(e.getNombre());
        }
        comboEmbarcacion.getSelectionModel().selectFirst();

        comboHorario.getItems().addAll("Matutino", "Medio día", "Vespertino");
        comboHorario.getSelectionModel().selectFirst();

        crearBotonesAsientos();

        comboEmbarcacion.setOnAction(e -> actualizarMatriz());
        comboHorario.setOnAction(e -> actualizarMatriz());

        actualizarMatriz();
    }

    private void crearBotonesAsientos() {
        botones = new Button[5][5];
        gridAsientos.getChildren().clear();

        gridAsientos.add(new Label(""), 0, 0);
        for (int c = 1; c <= 5; c++) {
            Label lblCol = new Label(String.valueOf(c));
            gridAsientos.add(lblCol, c, 0);
        }

        char letraFila = 'A';

        for (int fila = 0; fila < 5; fila++) {
            Label lblFila = new Label(String.valueOf(letraFila));
            gridAsientos.add(lblFila, 0, fila + 1);

            for (int col = 0; col < 5; col++) {
                Button btn = new Button(" ");
                btn.setMinSize(40, 40);
                btn.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

                int f = fila;
                int c = col;

                btn.setOnAction(e -> clickAsiento(f, c));

                botones[fila][col] = btn;
                gridAsientos.add(btn, col + 1, fila + 1);
            }
            letraFila++;
        }
    }

    private Embarcacion getEmbarcacionActual() {
        int index = comboEmbarcacion.getSelectionModel().getSelectedIndex();
        return embarcaciones[index];
    }


    private int[][] getMatrizActual() {
        Embarcacion e = getEmbarcacionActual();
        String horario = comboHorario.getSelectionModel().getSelectedItem();

        if (horario.equals("Matutino")) {
            return e.getAsientosMatutino();
        } else if (horario.equals("Medio día")) {
            return e.getAsientosMediodia();
        } else {
            return e.getAsientosVespertino();
        }
    }

    private void actualizarMatriz() {
        int[][] matriz = getMatrizActual();
        asientosSeleccionados.clear();
        actualizarLabels();

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 5; col++) {
                Button btn = botones[fila][col];

                if (matriz[fila][col] == 1) {
                    btn.setText("X");
                    btn.setDisable(true);
                    btn.setStyle("-fx-background-color: lightgray;");
                } else {
                    btn.setText(" ");
                    btn.setDisable(false);
                    btn.setStyle("");
                }
            }
        }
    }

    private void clickAsiento(int fila, int col){
        int[][] matriz = getMatrizActual();

        if (!getEmbarcacionActual().asientoDisponible(matriz, fila, col)) {
            return;
        }

        String nombreAsiento = convertirAsiento(fila, col);
        Button btn = botones[fila][col];

        if (asientosSeleccionados.contains(nombreAsiento)) {
            asientosSeleccionados.remove(nombreAsiento);
            btn.setStyle("");
        } else {

            asientosSeleccionados.add(nombreAsiento);
            btn.setStyle("-fx-background-color: lightgreen;");
        }

        actualizarLabels();
    }

    private String convertirAsiento(int fila, int col) {
        char letra = (char) ('A' + fila);
        int numero = col + 1;
        return "" + letra + numero;
    }

    private void actualizarLabels() {
        if (asientosSeleccionados.isEmpty()) {
            lblAsientosSeleccionados.setText("Asientos seleccionados: (ninguno)");
            lblTotal.setText("Total a pagar: $0");
        } else {
            lblAsientosSeleccionados.setText("Asientos seleccionados: " + asientosSeleccionados);
            int total = asientosSeleccionados.size() * VALOR_PASAJE;
            lblTotal.setText("Total a pagar: $" + total);
        }
    }

    @FXML
    public void confirmarCompra() {
        if (asientosSeleccionados.isEmpty()) {
            mostrarMensaje("Aviso", "No has seleccionado ningún asiento.");
            return;
        }

        Embarcacion e = getEmbarcacionActual();
        String horario = comboHorario.getSelectionModel().getSelectedItem();
        int[][] matriz = getMatrizActual();

        for (String asiento : asientosSeleccionados) {
            int fila = asiento.charAt(0) - 'A';
            int col = Character.getNumericValue(asiento.charAt(1)) - 1;
            e.reservarAsiento(matriz, fila, col);
        }

        int total = asientosSeleccionados.size() * VALOR_PASAJE;

        guardarVenta(e.getNombre(), horario, asientosSeleccionados, total);

        mostrarMensaje("Compra realizada",
                "Asientos: " + asientosSeleccionados + "\nTotal: $" + total);

        actualizarMatriz();
    }

    @FXML
    public void verResumen() {
        File archivo = new File("ventas.txt");
        if (!archivo.exists()) {
            mostrarMensaje("Resumen", "Todavía no hay ventas registradas.");
            return;
        }

        StringBuilder texto = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                texto.append(linea).append("\n");
            }
        } catch (IOException e) {
            mostrarMensaje("Error", "No se pudo leer el archivo: " + e.getMessage());
            return;
        }

        TextArea area = new TextArea(texto.toString());
        area.setEditable(false);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resumen de ventas");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(area);
        alert.showAndWait();
    }

    private void guardarVenta(String embarcacion, String horario,
                              ArrayList<String> asientos, int total) {
        try (FileWriter fw = new FileWriter("ventas.txt", true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println("Embarcación: " + embarcacion
                    + " | Horario: " + horario
                    + " | Asientos: " + asientos
                    + " | Total: $" + total);
        } catch (IOException e) {
            mostrarMensaje("Error", "No se pudo guardar la venta: " + e.getMessage());
        }
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
