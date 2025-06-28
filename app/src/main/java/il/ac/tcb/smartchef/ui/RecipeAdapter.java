package il.ac.tcb.smartchef.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import il.ac.tcb.smartchef.data.model.Recipe;
import il.ac.tcb.smartchef.databinding.ItemRecipeBinding;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.VH> {
    private List<Recipe> data;

    public RecipeAdapter(List<Recipe> data) {
        this.data = data;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecipeBinding b = ItemRecipeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(b);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Recipe r = data.get(position);
        holder.binding.tvTitle.setText(r.getTitle());
    }

    @Override public int getItemCount() {
        return data.size();
    }

    public void updateData(List<Recipe> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemRecipeBinding binding;
        VH(ItemRecipeBinding b) {
            super(b.getRoot());
            binding = b;
        }
    }
}
