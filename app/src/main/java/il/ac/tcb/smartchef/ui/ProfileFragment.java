package il.ac.tcb.smartchef.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import il.ac.tcb.smartchef.auth.LoginActivity;
import il.ac.tcb.smartchef.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding b;

    @Override
    public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle s) {
        b = FragmentProfileBinding.inflate(i, c, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle s) {
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if (u != null) {
            b.tvEmail.setText(u.getEmail());
            b.tvUid.setText("UID: " + u.getUid());
            if (u.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(u.getPhotoUrl())
                        .circleCrop()
                        .into(b.ivProfile);
            }
        }
        b.btnLogout.setOnClickListener(bt -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(requireContext(), LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK));
            requireActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        b = null;
    }
}


