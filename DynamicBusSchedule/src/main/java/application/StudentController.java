package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Bus;

import java.sql.*;

public class StudentController {

    @FXML private TableView<Bus> busTable;
    @FXML private TableColumn<Bus, String> busNumberCol;
    @FXML private TableColumn<Bus, String> departureCol;
    @FXML private TableColumn<Bus, String> fromCol;
    @FXML private TableColumn<Bus, String> toCol;
    @FXML private TableColumn<Bus, String> dayCol;

    private final ObservableList<Bus> busList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        busNumberCol.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        departureCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("fromPlace"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("toPlace"));
        dayCol.setCellValueFactory(new PropertyValueFactory<>("dayType"));
        loadBuses();
    }

    private void loadBuses() {
        busList.clear();
        try (Connection conn = DBUtil.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM buses ORDER BY day_type, departure_time");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                busList.add(new Bus(
                        rs.getInt("id"),
                        rs.getString("bus_number"),
                        rs.getString("departure_time"),
                        rs.getString("from_place"),
                        rs.getString("to_place"),
                        rs.getString("day_type")
                ));
            }
            busTable.setItems(busList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
