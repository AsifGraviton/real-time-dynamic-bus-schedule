package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        DBUtil.init(); // create DB and seed if needed
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        primaryStage.setTitle("Dynamic Bus Schedule");
        primaryStage.setScene(new Scene(root, 420, 320));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
