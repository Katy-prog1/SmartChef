package il.ac.tcb.smartchef.ui;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.function.Consumer;

/** A convenient TextWatcher that calls the Consumer passed by the lambda in onTextChanged*/
public class SimTextWatcher implements TextWatcher {
    private final Consumer<CharSequence> onText;
    public SimTextWatcher(Consumer<CharSequence> onText) {
        this.onText = onText;
    }
    @Override public void beforeTextChanged(CharSequence s, int st, int c1, int c2) {}
    @Override public void afterTextChanged(Editable e) {}
    @Override
    public void onTextChanged(CharSequence s, int st, int b, int c) {
        onText.accept(s);
    }
}
