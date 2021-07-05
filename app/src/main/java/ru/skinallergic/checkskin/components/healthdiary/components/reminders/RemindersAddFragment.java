package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindAddViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindersViewModel;
import ru.skinallergic.checkskin.databinding.FragmentRemindersAddBinding;

import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogTheme;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;

public class RemindersAddFragment extends BaseRemindersFragment {
    private  DialogFragment dialogfragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyViewModelFactory viewModelFactory = App.getInstance().getAppComponent().getViewModelFactory();
        FragmentRemindersAddBinding binding= FragmentRemindersAddBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(getViewModel());
        initBackGround(binding.background);

        binding.redactTime.setOnClickListener((View v)-> {
            dialogfragment.show(getParentFragmentManager(),"time");


        });
        binding.redactRemind.setOnClickListener((View v)-> {
            Navigation.findNavController(v).navigate(R.id.remindersPeriodFragment);

        });
        binding.redactRepeat.setOnClickListener((View v)-> {
            Navigation.findNavController(v).navigate(R.id.remindersRepeatFragment);

        });

        getDateViewModel().dateLive.observe(getViewLifecycleOwner(),(Date d)-> {
            String date=getDateViewModel().getDate(d);
            binding.date.setText(date);
        });


        dialogfragment = new TimePickerDialogTheme(getViewModel().getTimeLive());
        getViewModel().getTimeLive().observe(getViewLifecycleOwner(), (String date) ->{
            binding.time.setText(date);
        });
        /*
        viewModel.getTimeLive().observe(getViewLifecycleOwner(), (String date) ->{
            binding.timeAdd.setText(date);
        });*/

        View view=binding.getRoot();
        return view;
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