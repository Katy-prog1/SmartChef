package il.ac.tcb.smartchef.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import il.ac.tcb.smartchef.R;
import il.ac.tcb.smartchef.data.model.ShoppingItem;
import il.ac.tcb.smartchef.data.repository.FirestoreRepository;

public class ShoppingAdapter extends ArrayAdapter<ShoppingItem> {
    private final LayoutInflater inflater;
    private final List<ShoppingItem> items;

    public ShoppingAdapter(@NonNull Context context, @NonNull List<ShoppingItem> data) {
        super(context, 0, data);
        this.inflater = LayoutInflater.from(context);
        this.items = data;
    }

    @NonNull @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_shopping, parent, false);
        }
        TextView tv = convertView.findViewById(R.id.tvItem);
        ImageButton btnDel = convertView.findViewById(R.id.btnDelete);

        ShoppingItem si = items.get(position);
        tv.setText(si.getName() + ": " + si.getAmount() + si.getUnit());

        btnDel.setOnClickListener(v -> {
            FirestoreRepository.getInstance()
                    .deleteShoppingItem(si.getId(),
                            unused -> {
                                items.remove(si);
                                notifyDataSetChanged();
                                Toast.makeText(getContext(), "Deleted \"" + si.getName() + "\"", Toast.LENGTH_SHORT).show();
                            },
                            error -> Toast.makeText(getContext(), "Delete error", Toast.LENGTH_SHORT).show()
                    );
        });
        return convertView;
    }
}