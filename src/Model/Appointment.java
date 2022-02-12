package Model;

import java.sql.*;
import java.text.SimpleDateFormat;


public class Appointment {
    private  int appointmentId;
    private  String title;
    private  String type;
    private  String located;
    private  Timestamp start;
    private  Timestamp end;
    private  String customer;
    private  String contact;
    private  Timestamp createDate;
    private  String createdBy;
    private  Timestamp lastUpdate;
    private  String updatedBy;
    private  String url;
    private  String description;
    private  int customerId;
    private  int userId;
    private  SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
    private  SimpleDateFormat date = new SimpleDateFormat("MMM/d");




    public Appointment(int appointmentId, int customerId, int userId, String title, String type, String located, Timestamp start, Timestamp end, String customer, String contact, Timestamp createDate, String createdBy, Timestamp lastUpdate, String updatedBy, String url, String description){
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.located = located;
        this.start = start;
        this.end = end;
        this.customer = customer;
        this.contact = contact;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.updatedBy = updatedBy;
        this.url = url;
        this.description = description;
    }

    public  String getTitle() { return title; }
    public  void setTitle(String title) {
        this.title = title;
    }
    public  String getType() {
        return type;
    }
    public  void setType(String type) {
        this.type = type;
    }
    public  String getLocated() { return located; }
    public  void setLocated(String located) {
        this.located = located;
    }
    public  Timestamp getStart() {
        return start;
    }
    public  void setStart(Timestamp start) {
        this.start = start;
    }
    public  Timestamp getEnd() {
        return end;
    }
    public  void setEnd(Timestamp end) { this.end = end; }
    public  String getCustomer() { return customer; }
    public  void setCustomer(String customer) { this.customer = customer; }
    public  String getContact() { return contact; }
    public  void setContact(String contact) { this.contact = contact; }
    public  Timestamp getCreateDate() { return createDate; }
    public  void setCreateDate(Timestamp createDate) { this.createDate = createDate; }
    public  String getCreatedBy() { return createdBy; }
    public  void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public  Timestamp getLastUpdate() { return lastUpdate; }
    public  void setLastUpdate(Timestamp lastUpdate) { this.lastUpdate = lastUpdate; }
    public  String getUpdatedBy() { return updatedBy; }
    public  void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
    public  String getUrl() { return url; }
    public  void setUrl(String url) { this.url = url; }
    public  String getDescription() { return description; }
    public  void setDescription(String description) { this.description = description; }
    public String getStartTime(){return sdf.format(start);}
    public String getEndTime(){return sdf.format(end);}
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    public int getCustomerId() { return customerId; }
    public int getUserId(){return userId;}
    public String getDate(){return date.format(start);}//It flags as unused method, but it's used by the populate table methods.

}
