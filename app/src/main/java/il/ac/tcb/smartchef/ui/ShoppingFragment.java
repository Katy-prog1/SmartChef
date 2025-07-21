package il.ac.tcb.smartchef.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import il.ac.tcb.smartchef.data.model.ShoppingItem;
import il.ac.tcb.smartchef.data.repository.FirestoreRepository;
import il.ac.tcb.smartchef.databinding.FragmentShoppingBinding;
import il.ac.tcb.smartchef.ui.viewmodel.ShoppingViewModel;

public class ShoppingFragment extends Fragment {
    private FragmentShoppingBinding binding;
    private ShoppingAdapter adapter;
    private ShoppingViewModel vm;
    private final List<ShoppingItem> items = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentShoppingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new ShoppingAdapter(requireContext(), items);
        binding.lvShopping.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(ShoppingViewModel.class);
        vm.getItems().observe(getViewLifecycleOwner(), list -> {
            items.clear();
            items.addAll(list);
            adapter.notifyDataSetChanged();
        });
        vm.loadItems();

        binding.btnAdd.setOnClickListener(v -> {
            String name  = binding.etNewItem.getText().toString().trim();
            String qtyStr= binding.etAmount.getText().toString().trim();
            String unit  = binding.spUnit.getSelectedItem().toString();

            if (name.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(getContext(),
                        "Fill all ", Toast.LENGTH_SHORT).show();
                return;
            }

            int qty;
            try {
                qty = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                binding.etAmount.setError("Incorrect number");
                return;
            }

            ShoppingItem newItem = new ShoppingItem(
                    name.toLowerCase(), name, qty, unit, false
            );
            FirestoreRepository.getInstance()
                    .addOrUpdateShoppingItem(
                            newItem,
                            unused -> {
                                Toast.makeText(getContext(),
                                        "Added to list", Toast.LENGTH_SHORT).show();
                                vm.loadItems();
                                binding.etNewItem.setText("");
                                binding.etAmount.setText("");
                            },
                            err -> {
                                Log.e("ShoppingFrag", "Error adding", err);
                                Toast.makeText(getContext(),
                                        "Adding Error", Toast.LENGTH_SHORT).show();
                            }
                    );
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
