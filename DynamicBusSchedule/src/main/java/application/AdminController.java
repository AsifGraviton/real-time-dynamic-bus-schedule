package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Bus;

import java.sql.*;

public class AdminController {

    @FXML private TableView<Bus> busTable;
    @FXML private TableColumn<Bus, String> busNumberCol;
    @FXML private TableColumn<Bus, String> departureCol;
    @FXML private TableColumn<Bus, String> fromCol;
    @FXML private TableColumn<Bus, String> toCol;
    @FXML private TableColumn<Bus, String> dayCol;

    @FXML private ComboBox<String> dayFilter;
    @FXML private TextField searchField;

    private final ObservableList<Bus> busList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        busNumberCol.setCellValueFactory(new PropertyValueFactory<>("busNumber"));
        departureCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        fromCol.setCellValueFactory(new PropertyValueFactory<>("fromPlace"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("toPlace"));
        dayCol.setCellValueFactory(new PropertyValueFactory<>("dayType"));

        dayFilter.getItems().addAll("All", "Weekday", "Friday", "Holiday");
        dayFilter.getSelectionModel().select("All");
        loadBuses();
    }

    private void loadBuses() {
        busList.clear();
        String sql = "SELECT * FROM buses";
        String filter = dayFilter.getSelectionModel().getSelectedItem();
        String search = searchField.getText() == null ? "" : searchField.getText().trim();

        boolean whereAdded = false;
        if (filter != null && !"All".equals(filter)) {
            sql += " WHERE day_type=?";
            whereAdded = true;
        }
        if (!search.isEmpty()) {
            sql += whereAdded ? " AND" : " WHERE";
            sql += " (bus_number LIKE ? OR from_place LIKE ? OR to_place LIKE ?)";
        }
        sql += " ORDER BY departure_time";

        try (Connection conn = DBUtil.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if (filter != null && !"All".equals(filter)) {
                ps.setString(idx++, filter);
            }
            if (!search.isEmpty()) {
                String k = "%" + search + "%";
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
                        rs.getString("day_type")
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
        TextField t1 = new TextField();
        TextField t2 = new TextField();
        TextField t3 = new TextField();
        TextField t4 = new TextField();
        ComboBox<String> c5 = new ComboBox<>();
        c5.getItems().addAll("Weekday", "Friday", "Holiday");
        c5.getSelectionModel().selectFirst();

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.addRow(0, l1, t1);
        grid.addRow(1, l2, t2);
        grid.addRow(2, l3, t3);
        grid.addRow(3, l4, t4);
        grid.addRow(4, l5, c5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                return new String[]{t1.getText(), t2.getText(), t3.getText(), t4.getText(), c5.getValue()};
            }
            return null;
        });

        dialog.showAndWait().ifPresent(vals -> {
            try (Connection conn = DBUtil.connect()) {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO buses(bus_number,departure_time,from_place,to_place,day_type) VALUES (?,?,?,?,?)");
                for (int i=0;i<5;i++) ps.setString(i+1, vals[i]);
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
