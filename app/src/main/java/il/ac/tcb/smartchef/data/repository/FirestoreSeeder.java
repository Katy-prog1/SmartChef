package il.ac.tcb.smartchef.data.repository;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import il.ac.tcb.smartchef.data.model.Ingredient;
import il.ac.tcb.smartchef.data.model.Recipe;
import il.ac.tcb.smartchef.data.model.ShoppingItem;

import java.util.Arrays;
import java.util.List;

public class FirestoreSeeder {
    private static final String TAG = "FirestoreSeeder";
    private static FirestoreSeeder instance;
    private final FirebaseFirestore db;

    private FirestoreSeeder() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FirestoreSeeder getInstance() {
        if (instance == null) {
            instance = new FirestoreSeeder();
        }
        return instance;
    }

    public void seedAll() {
        seedIngredients();
        seedRecipes();
        seedShoppingItems();
    }

    private void seedIngredients() {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("Flour", 500, "g"),
                new Ingredient("Sugar", 200, "g"),
                new Ingredient("Salt", 5, "g"),
                new Ingredient("Milk", 1000, "ml"),
                new Ingredient("Egg", 1, "pcs")
        );
        for (Ingredient ing : ingredients) {
            db.collection("ingredients")
                    .document(ing.getName().toLowerCase())
                    .set(ing)
                    .addOnSuccessListener(aVoid -> Log.i(TAG, "Ingredient seeded: " + ing.getName()))
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to seed ingredient: " + ing.getName(), e));
        }
    }

    private void seedRecipes() {
        List<Ingredient> pancakeIngredients = Arrays.asList(
                new Ingredient("Flour", 200, "g"),
                new Ingredient("Milk", 300, "ml"),
                new Ingredient("Egg", 2, "pcs"),
                new Ingredient("Salt", 1, "g")
        );
        Recipe pancakes = new Recipe(
                "pancakes",
                "Pancakes",
                pancakeIngredients,
                "Mix all ingredients and fry on a hot pan until golden brown.",
                false
        );
        db.collection("recipes")
                .document(pancakes.getId())
                .set(pancakes)
                .addOnSuccessListener(aVoid -> Log.i(TAG, "Recipe seeded: " + pancakes.getTitle()))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to seed recipe: " + pancakes.getTitle(), e));
    }

    private void seedShoppingItems() {
        List<ShoppingItem> items = Arrays.asList(
                new ShoppingItem("milk", "Milk", 1, "L", false),
                new ShoppingItem("flour", "Flour", 1, "kg", false),
                new ShoppingItem("eggs", "Eggs", 12, "pcs", false)
        );
        for (ShoppingItem item : items) {
            db.collection("shopping")
                    .document(item.getId())
                    .set(item)
                    .addOnSuccessListener(aVoid -> Log.i(TAG, "ShoppingItem seeded: " + item.getName()))
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to seed shopping item: " + item.getName(), e));
        }
    }
}
