import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;
import java.io.*;
import java.util.ArrayList;

public class EcoIslaController {

    @FXML private ComboBox<String> comboEmbarcacion;
    @FXML private ComboBox<String> comboHorario;
    @FXML private GridPane grid;
    @FXML private Label lblAsientos;
    @FXML private Label lblTotal;

    private Embarcacion[] embarcaciones;
    private Button[][] botones;
    private ArrayList<String> seleccion = new ArrayList<>();
    private final int precio = 15000;

    @FXML
    public void initialize() {
        embarcaciones = new Embarcacion[3];
        embarcaciones[0] = new Embarcacion("EcoIsla I");
        embarcaciones[1] = new Embarcacion("EcoIsla II");
        embarcaciones[2] = new Embarcacion("EcoIsla III");

        comboEmbarcacion.getItems().addAll("EcoIsla I", "EcoIsla II", "EcoIsla III");
        comboEmbarcacion.getSelectionModel().selectFirst();

        comboHorario.getItems().addAll("Matutino", "Medio día", "Vespertino");
        comboHorario.getSelectionModel().selectFirst();

        crearMatriz();
        cargarAsientos();

        comboEmbarcacion.setOnAction(e -> cargarAsientos());
        comboHorario.setOnAction(e -> cargarAsientos());
    }

    private void crearMatriz() {
        grid.getChildren().clear();
        botones = new Button[5][5];

        char letra = 'A';
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                Button b = new Button(" ");
                b.setMinSize(40, 40);
                int fi = f, co = c;
                b.setOnAction(e -> click(fi, co));
                botones[f][c] = b;
                grid.add(b, c, f);
            }
            letra++;
        }
    }

    private int[][] getMatriz() {
        Embarcacion e = embarcaciones[comboEmbarcacion.getSelectionModel().getSelectedIndex()];
        String h = comboHorario.getSelectionModel().getSelectedItem();
        if (h.equals("Matutino")) return e.getMatutino();
        if (h.equals("Medio día")) return e.getMediodia();
        return e.getVespertino();
    }

    private void cargarAsientos() {
        seleccion.clear();
        actualizarLabels();
        int[][] m = getMatriz();
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                Button b = botones[f][c];
                if (m[f][c] == 1) {
                    b.setText("X");
                    b.setDisable(true);
                } else {
                    b.setText(" ");
                    b.setDisable(false);
                    b.setStyle("");
                }
            }
        }
    }

    private void click(int f, int c) {
        int[][] m = getMatriz();
        Embarcacion e = embarcaciones[comboEmbarcacion.getSelectionModel().getSelectedIndex()];

        String nombre = "" + (char)('A' + f) + (c + 1);
        Button b = botones[f][c];

        if (seleccion.contains(nombre)) {
            seleccion.remove(nombre);
            b.setStyle("");
        } else {
            if (e.disponible(m, f, c)) {
                seleccion.add(nombre);
                b.setStyle("-fx-background-color: lightgreen;");
            }
        }
        actualizarLabels();
    }

    private void actualizarLabels() {
        lblAsientos.setText("Asientos seleccionados: " + seleccion);
        lblTotal.setText("Total: $" + (seleccion.size() * precio));
    }

    @FXML
    public void confirmar() {
        if (seleccion.isEmpty()) {
            alerta("Aviso", "No has seleccionado asientos.");
            return;
        }

        int[][] m = getMatriz();
        Embarcacion e = embarcaciones[comboEmbarcacion.getSelectionModel().getSelectedIndex()];

        for (String s : seleccion) {
            int f = s.charAt(0) - 'A';
            int c = Character.getNumericValue(s.charAt(1)) - 1;
            e.reservar(m, f, c);
        }

        int total = seleccion.size() * precio;
        guardar(e.getNombre(), comboHorario.getValue(), seleccion, total);

        alerta("Compra realizada", "Total pagado: $" + total);
        cargarAsientos();
    }

    private void guardar(String emb, String h, ArrayList<String> asientos, int total) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("ventas.txt", true))) {
            pw.println("Embarcación: " + emb + " | Horario: " + h + " | Asientos: " + asientos + " | Total: $" + total);
        } catch (Exception ex) {}
    }

    private void alerta(String t, String m) {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}
