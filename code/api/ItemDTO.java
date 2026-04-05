package api;

public class ItemDTO {
    private String name;
    private float price;
    private int quantity_unit;

    public ItemDTO(){}

    public ItemDTO (String name, float price, int quantityUnit){
        this.name = name;
        this.price = price;
        this.quantity_unit = quantityUnit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity_unit() {
        return quantity_unit;
    }

    public void setQuantity_unit(int quantity_unit) {
        this.quantity_unit = quantity_unit;
    }

}
