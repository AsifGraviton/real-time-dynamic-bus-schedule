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

    public Bus(int id, String busNumber, String departureTime, String fromPlace, String toPlace, String dayType) {
        this.id = new SimpleIntegerProperty(id);
        this.busNumber = new SimpleStringProperty(busNumber);
        this.departureTime = new SimpleStringProperty(departureTime);
        this.fromPlace = new SimpleStringProperty(fromPlace);
        this.toPlace = new SimpleStringProperty(toPlace);
        this.dayType = new SimpleStringProperty(dayType);
    }

    public int getId() { return id.get(); }
    public String getBusNumber() { return busNumber.get(); }
    public String getDepartureTime() { return departureTime.get(); }
    public String getFromPlace() { return fromPlace.get(); }
    public String getToPlace() { return toPlace.get(); }
    public String getDayType() { return dayType.get(); }
}
