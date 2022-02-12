package Model;

import java.sql.Timestamp;

public class Customer {
    private int customerId;
    private String name;
    private String address;
    private String address2;
    private int active;
    private Timestamp createDate;
    private String createdBy;
    private  Timestamp lastUpdate;
    private String lastUpdateBy;
    private String phone;
    private String city;
    private String country;
    private String postal;
    private int cityId;
    private int countryId;
    private int addressId;

    public Customer(int customerId, String name, String address, String address2, int active,
                    Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy,
                    String phone, String city, String country, String postal,
                    int cityId, int countryId, int addressId) {
        this.customerId = customerId;
        this.name = name;
        this.address = address;
        this.address2 = address2;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.postal = postal;
        this.cityId = cityId;
        this.countryId = countryId;
        this.addressId = addressId;
    }

    public int getCustomerId() {
        return customerId;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public String getAddress2() {
        return address2;
    }
    public int getActive() {
        return active;
    }
    public Timestamp getCreateDate() {
        return createDate;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
    public String getPhone() {
        return phone;
    }
    public String getCity() {
        return city;
    }
    public String getCountry() {
        return country;
    }
    public String getPostal() {
        return postal;
    }
    public int getCityId() {
        return cityId;
    }
    public int getCountryId() {
        return countryId;
    }
    public int getAddressId() {
        return addressId;
    }
    public String toString(){return name;}
    public boolean equals(Customer c) {
        if (c.getCustomerId() == this.customerId)
            return true;
        return false;
    }

    public boolean equals(int id){
        if (this.getCustomerId() == id)
            return true;
        return false;
    }

}
