import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.io.*;

public class MenuController {

    @FXML
    public void abrirVenta() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("EcoIslaGUI.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Venta de boletos");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirResumen() {
        File archivo = new File("ventas.txt");
        if (!archivo.exists()) {
            mostrar("Resumen", "No hay ventas registradas.");
            return;
        }
        StringBuilder texto = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) texto.append(linea).append("\n");
        } catch (Exception e) {
            mostrar("Error", e.getMessage());
        }
        TextArea area = new TextArea(texto.toString());
        area.setEditable(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resumen");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(area);
        alert.showAndWait();
    }

    @FXML
    public void salir() {
        Platform.exit();
    }

    private void mostrar(String t, String m) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }
}
