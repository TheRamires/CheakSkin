package ru.skinallergic.checkskin.components.home.lpu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skinallergic.checkskin.components.home.data.LPU;
import ru.skinallergic.checkskin.components.home.viewmodels.HomeViewModel;
import ru.skinallergic.checkskin.databinding.FragmentLPUReviewBinding;

public class LPUReviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLPUReviewBinding binding=FragmentLPUReviewBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();

        HomeViewModel homeViewModel=new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        int idPosition=getArguments().getInt("idPosition");
        LPU lpuEntity= homeViewModel.lpuLive.getValue().get(idPosition);
        binding.setEntity(lpuEntity);

        return view;
    }
    public void backstack(View view){
        Navigation.findNavController(view).popBackStack();
    }
}