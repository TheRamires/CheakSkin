package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindAddViewModel;
import ru.skinallergic.checkskin.databinding.FragmentRemindersAddBinding;

import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogTheme;

public class RemindersAddFragment extends BaseRemindersFragment {
    private  DialogFragment dialogfragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRemindersAddBinding binding= FragmentRemindersAddBinding.inflate(inflater);
        binding.setFragment(this);
        RemindAddViewModel viewModel=new ViewModelProvider(requireActivity()).get(RemindAddViewModel.class);

        dialogfragment = new TimePickerDialogTheme(viewModel.getTimeLive());
        viewModel.getTimeLive().observe(getViewLifecycleOwner(), (String date) ->{
            binding.timeAdd.setText(date);
        });

        View view=binding.getRoot();
        return view;
    }
    public void backStack( View view){
        Navigation.findNavController(view).popBackStack();
    }
    public void redactTime(View view){
        dialogfragment.show(requireActivity().getSupportFragmentManager(), "theme2");

    }
    public void redactRemind(View view){
        Navigation.findNavController(view).navigate(R.id.action_remindersAddFragment_to_remindersPeriodFragment);

    }
    public void redactRepeat (View view){
        Navigation.findNavController(view).navigate(R.id.action_remindersAddFragment_to_remindersRepeatFragment);
    }
}