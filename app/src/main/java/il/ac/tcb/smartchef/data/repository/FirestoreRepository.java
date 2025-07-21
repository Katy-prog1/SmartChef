package il.ac.tcb.smartchef.data.repository;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import il.ac.tcb.smartchef.data.model.Recipe;
import il.ac.tcb.smartchef.data.model.ShoppingItem;

public class FirestoreRepository {
    private static FirestoreRepository instance;
    private final FirebaseFirestore db;

    private FirestoreRepository() {
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FirestoreRepository getInstance() {
        if (instance == null) {
            instance = new FirestoreRepository();
        }
        return instance;
    }

    private String getUid() {
        var user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            throw new IllegalStateException("User must be signed in to access Firestore data");
        }
        return user.getUid();
    }

    /** listen to the current user's recipe collection */
    public ListenerRegistration listenRecipes(EventListener<QuerySnapshot> listener) {
        String uid = getUid();
        return db
                .collection("users")
                .document(uid)
                .collection("recipes")
                .addSnapshotListener(listener);
    }

    /** add or update the current user's recipe */
    public void addOrUpdateRecipe(
            Recipe r,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure
    ) {
        String uid = getUid();
        db.collection("users")
                .document(uid)
                .collection("recipes")
                .document(r.getId())
                .set(r)
                .addOnSuccessListener(unused -> {
                    Log.i("Firestore", "Recipe saved OK: " + r.getId());
                    onSuccess.onSuccess(unused);
                })
                .addOnFailureListener(error -> {
                    Log.e("Firestore", "Save failed: " + r.getId(), error);
                    onFailure.onFailure(error);
                });
    }

    /** Получить один рецепт как объект Recipe */
    public void getRecipeById(
            String recipeId,
            OnSuccessListener<Recipe> onSuccess,
            OnFailureListener onFailure
    ) {
        String uid = getUid();
        db.collection("users")
                .document(uid)
                .collection("recipes")
                .document(recipeId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    Recipe recipe = snapshot.toObject(Recipe.class);
                    onSuccess.onSuccess(recipe);
                })
                .addOnFailureListener(onFailure);
    }

    /** listen to the current user's shopping list */
    public ListenerRegistration listenShopping(EventListener<QuerySnapshot> listener) {
        String uid = getUid();
        return db
                .collection("users")
                .document(uid)
                .collection("shopping")
                .addSnapshotListener(listener);
    }

    /** add or update a shopping list item */
    public void addOrUpdateShoppingItem(
            ShoppingItem item,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure
    ) {
        String uid = getUid();
        db.collection("users")
                .document(uid)
                .collection("shopping")
                .document(item.getId())
                .set(item)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    /** delete shopping list item */
    public void deleteShoppingItem(
            String itemId,
            OnSuccessListener<Void> onSuccess,
            OnFailureListener onFailure) {
        String uid = getUid();
        db.collection("users")
                .document(uid)
                .collection("shopping")
                .document(itemId)
                .delete()
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }
}
