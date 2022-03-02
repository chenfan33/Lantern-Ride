package edu.upenn.cis350.cisproject.data;

import java.io.Serializable;
import java.util.Date;

// implement Serializable to enable transfer objects btw activities
@SuppressWarnings("serial")
public class Request implements Serializable {
    Date date;
    String pickUpLoc;
    String dropOffLoc;
    int numOfPassengers;

    boolean completed;
    boolean markStatus;
    String driverID;
    String driverName;
    String passID;
    String passName;

    public Request(String pickUpLoc, String dropOffLoc, int numOfPassengers, Date date){
        this.pickUpLoc = pickUpLoc;
        this.dropOffLoc = dropOffLoc;
        this.numOfPassengers = numOfPassengers;
        this.date = date;
    }


    public Request(String pickUpLoc, String dropOffLoc, Date date, String driverName, String driverID, int numOfPassengers){
        this.pickUpLoc = pickUpLoc;
        this.dropOffLoc = dropOffLoc;
        this.date = date;
        this.driverName = driverName;
        this.driverID = driverID;
        this.numOfPassengers = numOfPassengers;
    }

    public String getPickUpLoc() {
        return pickUpLoc;
    }

    public String getDropOffLoc() {
        return dropOffLoc;
    }

    public int getNumOfPassengers() {
        return numOfPassengers;
    }

    public Date getDate() { return date; }

    public boolean getCompleted() { return completed; }

    public Boolean getMarkStatus() {
        return markStatus;
    }

    public String getDriverID() { return driverID; }

    public String getPassID() { return passID; }

    public String getDriverName() { return driverName; }

    public String getPassName() { return passName; }

    public void setCompleted(Boolean completed){ this.completed = completed;}

    public void setMarkStatus(Boolean markStatus) {
        this.markStatus = markStatus;
    }

    public void setDriverID(String driverID){ this.driverID = driverID;}

    public void setDriverName(String driverName) {this.driverName = driverName;}

    public void setPassID(String driverID){ this.driverID = passID;}

    public void setPassName(String driverName) {this.driverName = passName;}

    public String toString(){
        return pickUpLoc + ", " + dropOffLoc + ", " + numOfPassengers;
    }
}
