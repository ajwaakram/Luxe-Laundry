package com.find.luxelaundary.Model;
public class SubService {
    private String id;
    private String serviceID;
    private String name;
    private String price;
    private String description;

    public SubService() {
    }

    public SubService(String id, String serviceID, String name, String price, String description) {
        this.id = id;
        this.serviceID = serviceID;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getId() { return id; }
    public String getServiceID() { return serviceID; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getDescription() { return description; }

    public void setId(String id) { this.id = id; }
    public void setServiceID(String serviceID) { this.serviceID = serviceID; }
    public void setName(String name) { this.name = name; }
    public void setPrice(String price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
}

