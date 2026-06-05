package com.find.luxelaundary.Model;


public class Service {
    private String id;
    private String serviceName;
    private String imgUrl;

    public Service() {
        // Required for Firebase
    }

    public Service(String id, String serviceName, String imgUrl) {
        this.id = id;
        this.serviceName = serviceName;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
