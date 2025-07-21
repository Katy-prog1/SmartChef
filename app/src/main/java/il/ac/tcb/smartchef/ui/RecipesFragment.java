package il.ac.tcb.smartchef.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import il.ac.tcb.smartchef.data.model.Ingredient;
import il.ac.tcb.smartchef.data.model.Recipe;
import il.ac.tcb.smartchef.data.model.ShoppingItem;
import il.ac.tcb.smartchef.data.repository.FirestoreRepository;
import il.ac.tcb.smartchef.databinding.DialogAddRecipeBinding;
import il.ac.tcb.smartchef.databinding.FragmentRecipesBinding;
import il.ac.tcb.smartchef.ui.viewmodel.RecipesViewModel;

public class RecipesFragment extends Fragment {
    private FragmentRecipesBinding binding;
    private RecipesAdapter adapter;
    private RecipesViewModel vm;
    private final List<Recipe> recipes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle bs) {
        super.onViewCreated(view, bs);

        // 1) Adapter
        adapter = new RecipesAdapter(requireContext(), recipes);
        vm = new ViewModelProvider(this).get(RecipesViewModel.class);
        vm.getRecipes().observe(getViewLifecycleOwner(), list -> {
            recipes.clear();
            recipes.addAll(list);
            adapter.notifyDataSetChanged();
        });
        // 2) Launching download from Firestore
        vm.loadRecipes();

        // 3) FAB: Adding a new recipe
        binding.fabAdd.setOnClickListener(x -> showAddDialog());

        // 4) planning (marked recipes)
        binding.fabPlan.setOnClickListener(x -> {
            var sel = adapter.getSelectedPositions();
            if (sel.isEmpty()) {
                Toast.makeText(getContext(),
                        "Выберите хотя бы один рецепт", Toast.LENGTH_SHORT).show();
                return;
            }
            for (int pos : sel) {
                Recipe r = recipes.get(pos);
                for (Ingredient ing : r.getIngredients()) {
                    ShoppingItem item = new ShoppingItem(
                            ing.getName().toLowerCase(),
                            ing.getName(),
                            ing.getAmount(),
                            ing.getUnit(),
                            false
                    );
                    // add to shopping via repository
                    FirestoreRepository.getInstance()
                            .addOrUpdateShoppingItem(
                                    item,
                                    unused -> {},
                                    error -> Log.e("RecipesFrag",
                                            "Failed to add " + ing.getName(), error)
                            );
                }
            }
            Toast.makeText(getContext(),
                    "Ingridients added", Toast.LENGTH_SHORT).show();
            sel.clear();
            adapter.notifyDataSetChanged();
        });
    }

    private void showAddDialog() {
        DialogAddRecipeBinding dlg = DialogAddRecipeBinding.inflate(
                LayoutInflater.from(requireContext())
        );
        List<Ingredient> ings = new ArrayList<>();
        ings.add(new Ingredient("", 0, "g"));
        List<String> units = Arrays.asList("g","kg","ml","L","tsp","tbsp","cup");
        IngredientAdapter ia = new IngredientAdapter(ings, units);
        dlg.rvIngredients.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );
        dlg.rvIngredients.setAdapter(ia);
        dlg.btnAddIngredient.setOnClickListener(v -> {
            ings.add(new Ingredient("", 0, units.get(0)));
            ia.notifyItemInserted(ings.size()-1);
            dlg.rvIngredients.scrollToPosition(ings.size()-1);
        });

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle("New recipe")
                .setView(dlg.getRoot())
                .setNegativeButton("Cancel", (d, w) -> d.dismiss())
                .setPositiveButton("Save", null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {
                    boolean error = dlg.etTitle.getText().toString().trim().isEmpty();
                    for (Ingredient ing : ings) {
                        if (ing.getName().trim().isEmpty() || ing.getAmount() <= 0) {
                            error = true;
                            break;
                        }
                    }
                    if (error) {
                        Toast.makeText(requireContext(),
                                "Please fill all fields!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Recipe r = new Recipe(
                            UUID.randomUUID().toString(),
                            dlg.etTitle.getText().toString().trim(),
                            ings,
                            dlg.etInstructions.getText().toString().trim(),
                            false
                    );
                    // save recies
                    FirestoreRepository.getInstance()
                            .addOrUpdateRecipe(r,
                                    unused -> {
                                        Toast.makeText(requireContext(),
                                                "Recipe saved", Toast.LENGTH_SHORT).show();
                                        vm.loadRecipes();
                                        dialog.dismiss();
                                    },
                                    err -> Log.e("RecipesFragment",
                                            "Error saving recipe", err)
                            );
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
