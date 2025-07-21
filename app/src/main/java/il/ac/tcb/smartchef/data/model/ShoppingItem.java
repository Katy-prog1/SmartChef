package il.ac.tcb.smartchef.data.model;

public class ShoppingItem {
    private String id;
    private String name;
    private double amount;
    private String unit;
    private boolean bought;

    public ShoppingItem() {}

    public ShoppingItem(String id, String name, double amount, String unit, boolean bought) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.bought = bought;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isBought() { return bought; }
    public void setBought(boolean bought) { this.bought = bought; }

    public double getAmount() {return amount; }

    public String getUnit() {return unit;}
}
