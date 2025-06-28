package il.ac.tcb.smartchef.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import il.ac.tcb.smartchef.data.model.Recipe;
import il.ac.tcb.smartchef.data.repository.FirestoreRepository;
import il.ac.tcb.smartchef.databinding.FragmentAddRecipeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddRecipeFragment extends Fragment {
    private FragmentAddRecipeBinding binding;
    private final FirestoreRepository repository = new FirestoreRepository();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);

        binding.btnSave.setOnClickListener(v -> saveRecipe());

        return binding.getRoot();
    }

    private void saveRecipe() {
        String title = binding.etTitle.getText().toString().trim();
        String ingreds = binding.etIngredients.getText().toString().trim();
        String instr  = binding.etInstructions.getText().toString().trim();

        if (title.isEmpty() || ingreds.isEmpty() || instr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> ingredients = Arrays.asList(ingreds.split("\\s*,\\s*"));
        String id = UUID.randomUUID().toString();
        Recipe recipe = new Recipe(id, title, ingredients, instr);

        repository.addRecipe(recipe, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Recipe saved", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(AddRecipeFragment.this).popBackStack();
                } else {
                    Toast.makeText(getContext(),
                            "Save failed: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
