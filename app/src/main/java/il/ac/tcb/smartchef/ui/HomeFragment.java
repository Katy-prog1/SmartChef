package il.ac.tcb.smartchef.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import il.ac.tcb.smartchef.databinding.FragmentHomeBinding;
import il.ac.tcb.smartchef.data.model.Recipe;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private RecipeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // setup RecyclerView
        adapter = new RecipeAdapter(new ArrayList<>());
        binding.recyclerRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerRecipes.setAdapter(adapter);

        // observe loading state
        viewModel.isLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            binding.recyclerRecipes.setVisibility(loading ? View.GONE : View.VISIBLE);
        });

        // observe recipe list
        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            adapter.updateData(recipes);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
