package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    @FXML private TableColumn<Bus, String> tripForCol;
    @FXML private TableColumn<Bus, String> driverNameCol;
    @FXML private TableColumn<Bus, String> driverNumberCol;
    @FXML private TextField searchField; // Add this line

    private final ObservableList<Bus> busList = FXCollections.observableArrayList();
    private FilteredList<Bus> filteredData;

    @FXML
    public void initialize() {
        // Initialize table columns
        busNumberCol.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        departureCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("fromPlace"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("toPlace"));
        dayCol.setCellValueFactory(new PropertyValueFactory<>("dayType"));
        tripForCol.setCellValueFactory(new PropertyValueFactory<>("tripFor"));
        driverNameCol.setCellValueFactory(new PropertyValueFactory<>("driverName"));
        driverNumberCol.setCellValueFactory(new PropertyValueFactory<>("driverNumber"));

        // Style tripFor column with badges
        tripForCol.setCellFactory(column -> new TableCell<Bus, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item.toLowerCase()) {
                        case "student":
                            setStyle("-fx-background-color: #e8f6f3; -fx-text-fill: #16a085; -fx-background-radius: 10; -fx-padding: 3 8; -fx-font-weight: bold; -fx-alignment: center;");
                            break;
                        case "teacher":
                            setStyle("-fx-background-color: #fef9e7; -fx-text-fill: #f39c12; -fx-background-radius: 10; -fx-padding: 3 8; -fx-font-weight: bold; -fx-alignment: center;");
                            break;
                        case "stuff":
                            setStyle("-fx-background-color: #f4ecf7; -fx-text-fill: #8e44ad; -fx-background-radius: 10; -fx-padding: 3 8; -fx-font-weight: bold; -fx-alignment: center;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        loadBuses();
        setupSearchFilter();
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
                        rs.getString("day_type"),
                        rs.getString("trip_for"),
                        rs.getString("driver_name"),
                        rs.getString("driver_number")
                ));
            }

            // Set up filtered list
            filteredData = new FilteredList<>(busList, p -> true);

            // Wrap the FilteredList in a SortedList
            SortedList<Bus> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(busTable.comparatorProperty());

            // Set the table items
            busTable.setItems(sortedData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {
        // Add listener to search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(bus -> {
                // If filter text is empty, show all buses
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Convert filter text to lower case for case-insensitive search
                String lowerCaseFilter = newValue.toLowerCase();

                // Check all fields for the search term
                if (bus.getBusNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (bus.getFromPlace().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (bus.getToPlace().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (bus.getDayType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (bus.getTripFor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (bus.getDriverName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (bus.getDriverNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (bus.getDepartureTime().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match
            });
        });
    }

    @FXML
    private void handleSearch() {
        // This method can be called if you want to trigger search on button click
        // For real-time search, we're using the textProperty listener above
    }

    @FXML
    private void handleRefresh() {
        loadBuses();
        searchField.clear(); // Clear search on refresh
    }
}