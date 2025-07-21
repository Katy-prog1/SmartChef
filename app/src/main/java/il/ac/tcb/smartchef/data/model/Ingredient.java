package il.ac.tcb.smartchef.data.model;


public class Ingredient {
    private String name;

    private double amount;
    private String unit;

    // empty constructor for firestore
    public Ingredient() {}

    public Ingredient(String name, double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {return name;}
    public double getAmount() {return amount;}
    public String getUnit() {return unit;}

    public void setName(String name) {this.name = name;}
    public void setAmount(Double amount) {this.amount = amount;}
    public void setUnit(String unit) {this.unit = unit;}
}


