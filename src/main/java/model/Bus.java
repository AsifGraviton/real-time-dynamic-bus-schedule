package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Bus {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty busNumber;
    private final SimpleStringProperty departureTime;
    private final SimpleStringProperty fromPlace;
    private final SimpleStringProperty toPlace;
    private final SimpleStringProperty dayType;
    private final SimpleStringProperty tripFor;
    private final SimpleStringProperty driverName;
    private final SimpleStringProperty driverNumber;

    public Bus(int id, String busNumber, String departureTime, String fromPlace, String toPlace, String dayType, String tripFor, String driverName, String driverNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.busNumber = new SimpleStringProperty(busNumber);
        this.departureTime = new SimpleStringProperty(departureTime);
        this.fromPlace = new SimpleStringProperty(fromPlace);
        this.toPlace = new SimpleStringProperty(toPlace);
        this.dayType = new SimpleStringProperty(dayType);
        this.tripFor = new SimpleStringProperty(tripFor);
        this.driverName = new SimpleStringProperty(driverName);
        this.driverNumber = new SimpleStringProperty(driverNumber);
    }

    public int getId() { return id.get(); }
    public String getBusNumber() { return busNumber.get(); }
    public String getDepartureTime() { return departureTime.get(); }
    public String getFromPlace() { return fromPlace.get(); }
    public String getToPlace() { return toPlace.get(); }
    public String getDayType() { return dayType.get(); }
    public String getTripFor() { return tripFor.get(); }
    public String getDriverName() { return driverName.get(); }
    public String getDriverNumber() { return driverNumber.get(); }
}