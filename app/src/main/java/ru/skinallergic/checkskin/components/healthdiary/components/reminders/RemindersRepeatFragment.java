package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.databinding.FragmentRemindersRepeatBinding;

public class RemindersRepeatFragment extends BaseRemindersFragment {
    private FragmentRemindersRepeatBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRemindersRepeatBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        initBackGround(binding.background);
        return view;
    }
}