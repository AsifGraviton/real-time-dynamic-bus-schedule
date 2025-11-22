package application;

import java.sql.*;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:bus_schedule.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void init() {
        try (Connection conn = connect();
             Statement st = conn.createStatement()) {

            st.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "role TEXT NOT NULL)");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS buses (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "bus_number TEXT NOT NULL, " +
                    "departure_time TEXT NOT NULL, " +
                    "from_place TEXT NOT NULL, " +
                    "to_place TEXT NOT NULL, " +
                    "day_type TEXT NOT NULL, " +
                    "trip_for TEXT NOT NULL, " +
                    "driver_name TEXT NOT NULL, " +
                    "driver_number TEXT NOT NULL)");

            // seed users
            PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users");
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                PreparedStatement ins = conn.prepareStatement("INSERT INTO users(username,password,role) VALUES (?,?,?)");
                ins.setString(1, "admin"); ins.setString(2, "admin123"); ins.setString(3, "admin"); ins.executeUpdate();
                ins.setString(1, "student"); ins.setString(2, "student"); ins.setString(3, "student"); ins.executeUpdate();
                ins.close();
            }
            rs.close();
            ps.close();

            // seed buses (only if empty)
            ps = conn.prepareStatement("SELECT COUNT(*) FROM buses");
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                PreparedStatement insB = conn.prepareStatement(
                        "INSERT INTO buses (bus_number, departure_time, from_place, to_place, day_type, trip_for, driver_name, driver_number) VALUES (?,?,?,?,?,?,?,?)");
                String[][] data = {
                        {"M3", "7:20 AM", "City", "Campus", "Weekday", "Student", "Michael Johnson", "(555) 123-4567"},
                        {"M6.4", "7:00 AM", "Campus", "City", "Weekday", "Teacher", "Sarah Williams", "(555) 234-5678"},
                        {"M2", "8:30 AM", "Campus", "City", "Weekday", "Student", "Robert Davis", "(555) 345-6789"},
                        {"M18", "3:00 PM", "Campus", "City", "Weekday", "Stuff", "Jennifer Miller", "(555) 456-7890"},
                        {"M5", "4:30 PM", "City", "Campus", "Weekday", "Student", "David Wilson", "(555) 567-8901"}
                };
                for (String[] row : data) {
                    for (int i=0;i<8;i++) insB.setString(i+1, row[i]);
                    insB.executeUpdate();
                }
                insB.close();
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}