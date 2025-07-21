package il.ac.tcb.smartchef.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import il.ac.tcb.smartchef.R;
import il.ac.tcb.smartchef.data.model.Ingredient;


public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.VH> {

    private final List<Ingredient> data;
    private final List<String> units; // g, kg ..

    public IngredientAdapter(List<Ingredient> data, List<String> units) {
        this.data = data;
        this.units = units;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Ingredient ing = data.get(pos);

        h.etName.setText(ing.getName());
        h.etAmount.setText(ing.getAmount()>0
                ? String.valueOf(ing.getAmount())
                : "");
        h.spUnit.setAdapter(new ArrayAdapter<>(
                h.itemView.getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                units));
        // set the current value if there is one
        if (units.contains(ing.getUnit())) {
            h.spUnit.setSelection(units.indexOf(ing.getUnit()));
        }
        // listen
        h.etName.addTextChangedListener(new SimTextWatcher(s ->
                ing.setName(s.toString())));
        h.etAmount.addTextChangedListener(new SimTextWatcher(s -> {
            try { ing.setAmount(Double.parseDouble(s.toString())); }
            catch (NumberFormatException ignored){}
        }));
        h.spUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override public void onItemSelected(AdapterView<?> p, View v, int i, long l){
                ing.setUnit(units.get(i));
            }
            @Override public void onNothingSelected(AdapterView<?> p){}
        });
    }

    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        EditText etName, etAmount;
        Spinner spUnit;
        VH(View item){
            super(item);
            etName   = item.findViewById(R.id.etIngName);
            etAmount = item.findViewById(R.id.etIngAmount);
            spUnit   = item.findViewById(R.id.spIngUnit);
        }
    }
}

