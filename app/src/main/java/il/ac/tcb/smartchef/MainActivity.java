package il.ac.tcb.smartchef;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import il.ac.tcb.smartchef.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Setting up NavController via NavHostFragment in layout
        NavHostFragment navHost = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(binding.navHostFragment.getId());
        if (navHost == null) {
            throw new IllegalStateException("NavHostFragment not found");
        }
        navController = navHost.getNavController();

        // which fragments are root
        appBarConfig = new AppBarConfiguration.Builder(
                R.id.recipesFragment,
                R.id.shoppingFragment,
                R.id.converterFragment,
                R.id.profileFragment
        ).build();

        // Bind the toolbar
        setSupportActionBar(binding.topAppBar);
        NavigationUI.setupWithNavController(binding.topAppBar, navController, appBarConfig);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfig)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // чтобы избежать утечки
    }
}
