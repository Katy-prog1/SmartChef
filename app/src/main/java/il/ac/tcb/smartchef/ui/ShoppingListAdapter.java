package il.ac.tcb.smartchef.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import il.ac.tcb.smartchef.databinding.ItemShoppingListBinding;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.VH> {
    private List<String> items;

    public ShoppingListAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShoppingListBinding binding = ItemShoppingListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.binding.tvItemName.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Обновить данные и перерисовать список */
    public void updateData(List<String> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemShoppingListBinding binding;

        VH(ItemShoppingListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
