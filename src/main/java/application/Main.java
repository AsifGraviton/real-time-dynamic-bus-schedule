package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        DBUtil.init(); // create DB and seed if needed

        // Load FXML with better error handling
        URL fxmlUrl = getClass().getResource("/view/login.fxml");
        if (fxmlUrl == null) {
            throw new RuntimeException("FXML file not found: /view/login.fxml");
        }

        Parent root = FXMLLoader.load(fxmlUrl);
        Scene scene = new Scene(root, 500, 500);

        // Try to load CSS
        try {
            URL cssUrl = getClass().getResource("/css/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.out.println("CSS file not found, running without styles");
            }
        } catch (Exception e) {
            System.out.println("Could not load CSS: " + e.getMessage());
        }

        primaryStage.setTitle("Dynamic Bus Schedule System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}