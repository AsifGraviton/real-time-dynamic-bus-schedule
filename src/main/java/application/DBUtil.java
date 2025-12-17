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
                        {"Bus 17", "7:00 AM", "Campus", "BoroMath", "Weekday", "Student", "Rahman", "01846372857"},
                        {"Bus 4,20", "7:30 AM", "Campus", "BoroMath", "Weekday", "Student", "Kabir , Sakib", "01946372857,01566372857"},
                        {"Bus 8", "8:00 AM", "Campus", "BoroMath", "Weekday", "Teacher", "Rahim", "01465372857"},
                        {"Bus 17", "7:30 AM", "BoroMath", "Campus", "Weekday", "Student", "Rahman", "01846372857"},
                        {"Bus 4,20", "8:00 AM", "BoroMath", "Campus", "Weekday", "Student", "kabir , Sakib", "01946372857,01566372857"},
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