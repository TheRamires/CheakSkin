package ru.skinallergic.checkskin.components.fooddiary.components;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skinallergic.checkskin.databinding.FragmentFoodIntakeBinding;

public class FoodIntakeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentFoodIntakeBinding binding= FragmentFoodIntakeBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();

        return view;
    }
    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
}