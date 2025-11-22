package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void handleLogin() {
        String u = usernameField.getText();
        String p = passwordField.getText();

        try (Connection conn = DBUtil.connect()) {
            PreparedStatement ps = conn.prepareStatement("SELECT role FROM users WHERE username=? AND password=?");
            ps.setString(1, u);
            ps.setString(2, p);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                Stage stage = (Stage) usernameField.getScene().getWindow();
                if ("admin".equalsIgnoreCase(role)) {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/admin.fxml")), 720, 520));
                } else {
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/student.fxml")), 720, 520));
                }
            } else {
                statusLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
