import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EcoIslaApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MenuPrincipal.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("EcoIsla Viajes");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
