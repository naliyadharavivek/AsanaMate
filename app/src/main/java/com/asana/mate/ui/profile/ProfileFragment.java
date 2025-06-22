package com.asana.mate.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.asana.mate.ConstantSP;
import com.asana.mate.MainActivity;
import com.asana.mate.databinding.FragmentProfileBinding;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    TextView nameShow, emailShow, genderShow, countryShow;
    Button logout, editProfile;
    SharedPreferences sp ;

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sp = requireActivity().getSharedPreferences(ConstantSP.PREF, Context.MODE_PRIVATE);

        nameShow = binding.profileNameShow;
        emailShow = binding.profileEmailShow;
        genderShow = binding.profileGenderShow;
        countryShow = binding.profileCountryShow;
        logout = binding.profileLogoutButton;
        editProfile = binding.profileEditButton;

        nameShow.setText(sp.getString(ConstantSP.NAME,""));
        emailShow.setText(sp.getString(ConstantSP.EMAIL,""));
        genderShow.setText(sp.getString(ConstantSP.GENDER,""));
        countryShow.setText(sp.getString(ConstantSP.COUNTRY,""));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.edit().clear().apply();

                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
