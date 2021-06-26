package ru.skinallergic.checkskin.intro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.databinding.FragmentIntro02Binding;

public class FragmentIntro02 extends Fragment implements OnBackPressedListener  {
    private IntroActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentIntro02Binding binding= FragmentIntro02Binding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        activity= (IntroActivity) requireActivity();

        return view;
    }
    public void clickButton(View view){
        activity.swipe(3);
    }

    @Override
    public void onBackPressed() {
        activity.swipe(1);
    }
}