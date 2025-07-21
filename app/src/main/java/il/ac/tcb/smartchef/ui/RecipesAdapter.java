package il.ac.tcb.smartchef.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.navigation.Navigation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import il.ac.tcb.smartchef.R;
import il.ac.tcb.smartchef.data.model.Recipe;

public class RecipesAdapter extends BaseAdapter {
    private final Context ctx;
    private final List<Recipe> recipes;
    private final Set<Integer> selected = new HashSet<>();

    public RecipesAdapter(Context ctx, List<Recipe> recipes) {
        this.ctx = ctx;
        this.recipes = recipes;
    }

    @Override public int getCount()        { return recipes.size(); }
    @Override public Object getItem(int i){ return recipes.get(i); }
    @Override public long getItemId(int i){ return i; }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View row = convertView!=null
                ? convertView
                : LayoutInflater.from(ctx).inflate(R.layout.item_recipe, parent, false);

        TextView tv = row.findViewById(R.id.tvRecipeTitle);
        CheckBox cb = row.findViewById(R.id.cbSelect);

        Recipe r = recipes.get(pos);
        tv.setText(r.getTitle());

        // Synchronize the checkbox with the selected positions
        cb.setOnCheckedChangeListener(null);
        cb.setChecked(selected.contains(pos));

        // When the user clicks on the checkbox itself:
        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selected.add(pos);
            else           selected.remove(pos);
        });

        // Clicking on the entire line (except the checkbox) will open the details:
        row.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("recipeId", r.getId());
            Navigation.findNavController(v)
                    .navigate(R.id.action_recipesFragment_to_recipeDetailFragment, args);
        });

        return row;
    }

    /** Accessing Selected Positions from a Fragment */
    public Set<Integer> getSelectedPositions() { return selected; }
}
