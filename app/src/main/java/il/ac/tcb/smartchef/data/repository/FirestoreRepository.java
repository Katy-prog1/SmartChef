package il.ac.tcb.smartchef.data.repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import il.ac.tcb.smartchef.data.model.Recipe;

public class FirestoreRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Add a recipe to Firestore collection "recipes"
     */
    public void addRecipe(Recipe recipe, OnCompleteListener<Void> listener) {
        db.collection("recipes")
                .document(recipe.getId())
                .set(recipe)
                .addOnCompleteListener(listener);
    }

    /**
     * Listen for realtime updates on "recipes" collection
     */
    public void listenRecipes(EventListener<QuerySnapshot> listener) {
        db.collection("recipes")
                .addSnapshotListener(listener);
    }
}
