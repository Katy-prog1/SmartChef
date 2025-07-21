package il.ac.tcb.smartchef.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import il.ac.tcb.smartchef.MainActivity;
import il.ac.tcb.smartchef.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        // if you are already logged in - go straight to Main
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            goToMain();
            return;
        }

        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String pass  = binding.etPassword.getText().toString();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please, fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                return;
            }

            // registration in Firebase
            binding.btnRegister.setEnabled(false);
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, task -> {
                        binding.btnRegister.setEnabled(true);
                        if (task.isSuccessful()) {
                            goToMain();
                        } else {
                            Toast.makeText(this,
                                    "Registration error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // the link "Already have an account?" returns to login
        binding.tvHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
