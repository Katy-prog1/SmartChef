package il.ac.tcb.smartchef.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import il.ac.tcb.smartchef.data.model.Recipe;
import il.ac.tcb.smartchef.data.repository.FirestoreRepository;

/**working with a list of recipes.
 * Responsible for loading data from Firestore and storing it in LiveData.**/
public class RecipesViewModel extends ViewModel {
    private final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
    private final FirestoreRepository repo = FirestoreRepository.getInstance();

    /** Return LiveData from recipes list */
    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    /** Starts downloading and subscribing to changes in the "recipes" collection. */
    public void loadRecipes() {
        repo.listenRecipes((QuerySnapshot snap, FirebaseFirestoreException err) -> {
            if (err != null) {
                return;
            }
            recipes.postValue(snap.toObjects(Recipe.class));
        });
    }
}
