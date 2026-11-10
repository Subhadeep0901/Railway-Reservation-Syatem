package com.example.railway.model;
import jakarta.persistence.*;
@Entity
@Table(name="trains")
public class Train{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;
private String trainNumber;
private String trainName;
private String source;
private String destination;
private String departureTime;
private String arrivalTime;
private int totalSeats;
private boolean active =true;

public Long getId(){
    return id;
}
public String getTrainNumber(){
    return trainNumber;
}
public void setTrainNumber(String trainNumber){
    this.trainNumber=trainNumber;
}

public String getTrainName(){
    return trainName;
}
public void setTrainName(String trainName){
    this.trainName=trainName;
}

public String getSource(){
    return source;
}
public void setSource(String source){
    this.source=source;
}

public String getDestination(){
    return destination;
}
public void setDestination(String destination){
    this.destination=destination;
}

public String getDepartureTime(){
    return departureTime;
}
public void setDepartureTime(String departureTime){
    this.departureTime=departureTime;
}

public String getArrivalTime(){
    return arrivalTime;
}
public void setArrivalTime(String arrivalTime){
    this.arrivalTime=arrivalTime;
}

public int getTotalSeats(){
    return totalSeats;
}
public void setTotalSeats(int totalSeats){
    this.totalSeats=totalSeats;
}

public boolean isActive(){
    return active;
}
public void setActive(boolean active){
    this.active=active;
}
}