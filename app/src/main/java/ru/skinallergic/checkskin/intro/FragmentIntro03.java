package ru.skinallergic.checkskin.intro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skinallergic.checkskin.databinding.FragmentIntro03Binding;

public class FragmentIntro03 extends Fragment implements OnBackPressedListener {
    private IntroActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentIntro03Binding binding= FragmentIntro03Binding.inflate(inflater);

        binding.setFragment(this);
        View view=binding.getRoot();
        activity= (IntroActivity) requireActivity();

        return view;
    }

    public void clickButton(View view){
        activity.swipe(4);
    }

    @Override
    public void onBackPressed() {
        activity.swipe(2);
    }
}