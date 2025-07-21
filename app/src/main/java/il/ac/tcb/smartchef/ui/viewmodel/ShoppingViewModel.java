package il.ac.tcb.smartchef.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import il.ac.tcb.smartchef.data.model.ShoppingItem;
import il.ac.tcb.smartchef.data.repository.FirestoreRepository;

public class ShoppingViewModel extends ViewModel {
    private final MutableLiveData<List<ShoppingItem>> items = new MutableLiveData<>();
    private final FirestoreRepository repo = FirestoreRepository.getInstance();

    /** returns the LiveData of the shopping list to be monitored from the fragment. */
    public LiveData<List<ShoppingItem>> getItems() {
        return items;
    }

    /** starts a subscription to the "shopping" collection in Firestore. */
    public void loadItems() {
        repo.listenShopping((QuerySnapshot snap, FirebaseFirestoreException err) -> {
            if (err != null) {
                return;
            }
            items.postValue(snap.toObjects(ShoppingItem.class));
        });
    }
}
