package com.asana.mate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.asana.mate.CategoryAdapter;
import com.asana.mate.YogaPose;
import com.asana.mate.YogaPoseAdapter;
import com.asana.mate.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    String[] modes = {"Easy", "Medium", "Hard"};
    String[] modesImages = {
            "https://raw.githubusercontent.com/naliyadharavivek/asnamate-media/55a9ad9e094cfbe74a53cb3a4a5b46790781d252/mode%20images/easy.png",
            "https://raw.githubusercontent.com/naliyadharavivek/asnamate-media/55a9ad9e094cfbe74a53cb3a4a5b46790781d252/mode%20images/medium.png",
            "https://raw.githubusercontent.com/naliyadharavivek/asnamate-media/55a9ad9e094cfbe74a53cb3a4a5b46790781d252/mode%20images/hard.png"
    };

    private FragmentHomeBinding binding;
    private List<YogaPose> yogaPoseList;
    private YogaPoseAdapter yogaPoseAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Setup category (horizontal)
        binding.homeCategory.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), modes, modesImages, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(String mode) {
                loadAsanasByMode(mode.toLowerCase()); // Ensure correct Firebase key
            }
        });
        binding.homeCategory.setAdapter(categoryAdapter);


        // Setup asana list (vertical)
        yogaPoseList = new ArrayList<>();
        yogaPoseAdapter = new YogaPoseAdapter(getActivity(), yogaPoseList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(yogaPoseAdapter);


        // Load 'easy' asanas by default
        loadAsanasByMode("easy");

        return root;
    }

    private void loadAsanasByMode(String modeKey) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(modeKey);

        yogaPoseList.clear(); // clear existing list before adding new
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot poseSnap : snapshot.getChildren()) {
                    YogaPose pose = poseSnap.getValue(YogaPose.class);
                    yogaPoseList.add(pose);
                }
                yogaPoseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to load asanas: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
