package il.ac.tcb.smartchef.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import il.ac.tcb.smartchef.data.model.Ingredient;
import il.ac.tcb.smartchef.data.model.Recipe;
import il.ac.tcb.smartchef.data.repository.FirestoreRepository;
import il.ac.tcb.smartchef.databinding.FragmentRecipeDetailBinding;

public class RecipeDetailFragment extends Fragment {
    private FragmentRecipeDetailBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get recipeId from arguments
        String recipeId = null;
        if (getArguments() != null) {
            recipeId = getArguments().getString("recipeId");
        }

        if (recipeId == null) {
            Toast.makeText(getContext(),
                            "No recipe ID provided", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        // Loading a recipe from Firestore
        FirestoreRepository.getInstance()
                .getRecipeById(recipeId,
                        recipe -> showRecipe(recipe),
                        error -> {
                            binding.tvDetailTitle.setText("Error loading recipe");
                            Toast.makeText(getContext(),
                                            "Failed to load recipe", Toast.LENGTH_SHORT)
                                    .show();
                        }
                );
    }

    private void showRecipe(Recipe r) {
        // Headline
        binding.tvDetailTitle.setText(r.getTitle());

        // Ingredients: Clear and add TextView dynamically
        binding.llIngredients.removeAllViews();
        for (Ingredient ing : r.getIngredients()) {
            TextView line = new TextView(requireContext());
            line.setText("• " + ing.getName() + ": " + ing.getAmount() + ing.getUnit());
            line.setTextSize(16);
            binding.llIngredients.addView(line);
        }

        // Instructions
        binding.tvDetailInstructions.setText(r.getInstructions());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
