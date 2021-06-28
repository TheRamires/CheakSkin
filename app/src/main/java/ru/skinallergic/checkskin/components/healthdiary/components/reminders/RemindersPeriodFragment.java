package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.databinding.FragmentRemindersPeriodBinding;

public class RemindersPeriodFragment extends BaseRemindersFragment {
    private FragmentRemindersPeriodBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRemindersPeriodBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();

        return view;
    }
    public void backStack (View view){
        Navigation.findNavController(view).popBackStack();
    }
}