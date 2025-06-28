package il.ac.tcb.smartchef.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import il.ac.tcb.smartchef.databinding.FragmentShoppingListBinding;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;


public class ShoppingListFragment extends Fragment {
    private FragmentShoppingListBinding binding;
    private ShoppingListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);

        // Инициализируем RecyclerView пустым списком
        adapter = new ShoppingListAdapter(new ArrayList<>());
        binding.recyclerShoppingList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerShoppingList.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
