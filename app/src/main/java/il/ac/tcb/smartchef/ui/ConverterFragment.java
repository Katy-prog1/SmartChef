package il.ac.tcb.smartchef.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.ArrayAdapter;

import il.ac.tcb.smartchef.R;
import il.ac.tcb.smartchef.databinding.FragmentConverterBinding;

public class ConverterFragment extends Fragment {

    private FragmentConverterBinding binding;

    private double toBase(String unit, double value) {
        switch (unit) {
            case "kg":       return value * 1000;
            case "ml":       return value;
            case "l":        return value * 1000;
            case "tablespoon": return value * 15;
            case "teaspoon":   return value * 5;
            case "cup (240мл)": return value * 240;
            case "gr":
            default:         return value;
        }
    }

    private double fromBase(String unit, double base) {
        switch (unit) {
            case "kg":       return base / 1000;
            case "ml":       return base;
            case "l":        return base / 1000;
            case "tablespoon": return base / 15;
            case "teaspoon":   return base / 5;
            case "cup (240мл)": return base / 240;
            case "gr":
            default:         return base;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConverterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.converter_units,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spFrom.setAdapter(adapter);
        binding.spTo.setAdapter(adapter);

        // Processing conversion
        binding.btnConvert.setOnClickListener(v -> {
            String inputStr = binding.etNumber.getText().toString().trim();
            if (TextUtils.isEmpty(inputStr)) {
                binding.etNumber.setError("Enter number");
                return;
            }
            double input = Double.parseDouble(inputStr);
            String unitFrom = binding.spFrom.getSelectedItem().toString();
            String unitTo   = binding.spTo.getSelectedItem().toString();

            double base   = toBase(unitFrom, input);
            double result = fromBase(unitTo, base);

            binding.tvResult.setText(String.format("%,.2f %s", result, unitTo));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
