package il.ac.tcb.smartchef.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import il.ac.tcb.smartchef.data.model.Recipe;
import il.ac.tcb.smartchef.data.repository.FirestoreRepository;

public class HomeViewModel extends ViewModel {
    private final FirestoreRepository repo = new FirestoreRepository();
    private final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);

    public HomeViewModel() {
        loadRecipes();
    }

    private void loadRecipes() {
        loading.setValue(true);
        repo.listenRecipes((snapshot, error) -> {
            if (snapshot != null && error == null) {
                List<Recipe> list = new ArrayList<>();
                for (var doc : snapshot.getDocuments()) {
                    Recipe r = doc.toObject(Recipe.class);
                    if (r != null) list.add(r);
                }
                recipes.setValue(list);
            }
            loading.setValue(false);
        });
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }
}
