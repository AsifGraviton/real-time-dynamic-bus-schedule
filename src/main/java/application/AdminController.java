package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.Bus;

import java.sql.*;

public class AdminController {

    @FXML private TableView<Bus> busTable;
    @FXML private TableColumn<Bus, String> busNumberCol;
    @FXML private TableColumn<Bus, String> departureCol;
    @FXML private TableColumn<Bus, String> fromCol;
    @FXML private TableColumn<Bus, String> toCol;
    @FXML private TableColumn<Bus, String> dayCol;
    @FXML private TableColumn<Bus, String> tripForCol;
    @FXML private TableColumn<Bus, String> driverNameCol;
    @FXML private TableColumn<Bus, String> driverNumberCol;

    @FXML private ComboBox<String> dayFilter;
    @FXML private ComboBox<String> tripFilter;
    @FXML private TextField searchField;

    private final ObservableList<Bus> busList = FXCollections.observableArrayList();

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

        // Initialize filters
        dayFilter.getItems().addAll("All", "Weekday", "Friday", "Saturday");
        dayFilter.getSelectionModel().select("All");

        tripFilter.getItems().addAll("All", "Student", "Teacher", "Stuff");
        tripFilter.getSelectionModel().select("All");

        loadBuses();
    }

    private void loadBuses() {
        busList.clear();
        String sql = "SELECT * FROM buses";
        String dayFilterValue = dayFilter.getSelectionModel().getSelectedItem();
        String tripFilterValue = tripFilter.getSelectionModel().getSelectedItem();
        String search = searchField.getText() == null ? "" : searchField.getText().trim();

        boolean whereAdded = false;
        if (dayFilterValue != null && !"All".equals(dayFilterValue)) {
            sql += " WHERE day_type=?";
            whereAdded = true;
        }
        if (tripFilterValue != null && !"All".equals(tripFilterValue)) {
            sql += whereAdded ? " AND" : " WHERE";
            sql += " trip_for=?";
            whereAdded = true;
        }
        if (!search.isEmpty()) {
            sql += whereAdded ? " AND" : " WHERE";
            sql += " (bus_number LIKE ? OR from_place LIKE ? OR to_place LIKE ? OR driver_name LIKE ?)";
        }
        sql += " ORDER BY departure_time";

        try (Connection conn = DBUtil.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if (dayFilterValue != null && !"All".equals(dayFilterValue)) {
                ps.setString(idx++, dayFilterValue);
            }
            if (tripFilterValue != null && !"All".equals(tripFilterValue)) {
                ps.setString(idx++, tripFilterValue);
            }
            if (!search.isEmpty()) {
                String k = "%" + search + "%";
                ps.setString(idx++, k);
                ps.setString(idx++, k);
                ps.setString(idx++, k);
                ps.setString(idx++, k);
            }

            ResultSet rs = ps.executeQuery();
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
            busTable.setItems(busList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        loadBuses();
    }

    @FXML
    private void handleFilterChanged() {
        loadBuses();
    }

    @FXML
    private void handleSearch() {
        loadBuses();
    }

    @FXML
    private void handleAddBus() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Add Bus");
        dialog.setHeaderText("Enter bus details");

        Label l1 = new Label("Bus No:");
        Label l2 = new Label("Time:");
        Label l3 = new Label("From:");
        Label l4 = new Label("To:");
        Label l5 = new Label("Day:");
        Label l6 = new Label("Trip For:");
        Label l7 = new Label("Driver Name:");
        Label l8 = new Label("Driver Number:");

        TextField t1 = new TextField();
        TextField t2 = new TextField();
        TextField t3 = new TextField();
        TextField t4 = new TextField();
        ComboBox<String> c5 = new ComboBox<>();
        c5.getItems().addAll("Weekday", "Friday", "Saturday");
        c5.getSelectionModel().selectFirst();

        ComboBox<String> c6 = new ComboBox<>();
        c6.getItems().addAll("Student", "Teacher", "Stuff");
        c6.getSelectionModel().selectFirst();

        TextField t7 = new TextField();
        TextField t8 = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, l1, t1);
        grid.addRow(1, l2, t2);
        grid.addRow(2, l3, t3);
        grid.addRow(3, l4, t4);
        grid.addRow(4, l5, c5);
        grid.addRow(5, l6, c6);
        grid.addRow(6, l7, t7);
        grid.addRow(7, l8, t8);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                return new String[]{
                        t1.getText(), t2.getText(), t3.getText(), t4.getText(),
                        c5.getValue(), c6.getValue(), t7.getText(), t8.getText()
                };
            }
            return null;
        });

        dialog.showAndWait().ifPresent(vals -> {
            try (Connection conn = DBUtil.connect()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO buses(bus_number,departure_time,from_place,to_place,day_type,trip_for,driver_name,driver_number) VALUES (?,?,?,?,?,?,?,?)");
                for (int i=0;i<8;i++) ps.setString(i+1, vals[i]);
                ps.executeUpdate();
                handleRefresh();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleRemoveBus() {
        Bus selected = busTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected bus?", ButtonType.OK, ButtonType.CANCEL);
        alert.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                try (Connection conn = DBUtil.connect()) {
                    PreparedStatement ps = conn.prepareStatement("DELETE FROM buses WHERE id=?");
                    ps.setInt(1, selected.getId());
                    ps.executeUpdate();
                    handleRefresh();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}