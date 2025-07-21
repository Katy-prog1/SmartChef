package il.ac.tcb.smartchef.data.model;

import java.util.List;

public class Recipe {
    private String id;
    private String title;
    private List<Ingredient> ingredients;
    private String instructions;
    private boolean toCook;

    public Recipe() {}

    public Recipe(String id, String title, List<Ingredient> ingredients, String instructions, Boolean toCook) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.toCook = toCook;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
