package com.find.luxelaundary.Model;

 
public class Cart {
    String id;
    String userId;
    String name;
    String price;
    int quantity;
    String instructions;

    public Cart() {}

    public Cart(String id, String userId, String name, String price,int quantity, String instructions) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.instructions = instructions;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPrice() { return price; }
     public int getQuantity() { return quantity; }
    public String getInstructions() { return instructions; }

    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setPrice(String price) { this.price = price; }
     public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}
