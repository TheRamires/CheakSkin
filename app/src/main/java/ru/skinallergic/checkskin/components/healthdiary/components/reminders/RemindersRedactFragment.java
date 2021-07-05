package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindersViewModel;
import ru.skinallergic.checkskin.databinding.FragmentReminderRedactBinding;

import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogTheme;

public class RemindersRedactFragment extends BaseRemindersFragment {
    private int position;
    private DialogFragment dialogFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentReminderRedactBinding binding=FragmentReminderRedactBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(getViewModel());
        initBackGround(binding.background);

        binding.redactTime.setOnClickListener((View v)-> {
            System.out.println("redact");
            dialogFragment.show(getParentFragmentManager(),"time");


        });
        binding.redactRemind.setOnClickListener((View v)-> {
            System.out.println("redact");
            Navigation.findNavController(v).navigate(R.id.remindersPeriodFragment);

        });
        binding.redactRepeat.setOnClickListener((View v)-> {
            System.out.println("redact");
            Navigation.findNavController(v).navigate(R.id.remindersRepeatFragment);

        });

        getDateViewModel().dateLive.observe(getViewLifecycleOwner(),(Date d)-> {
            String date=getDateViewModel().getDate(d);
            binding.date.setText(date);
        });


        dialogFragment = new TimePickerDialogTheme(getViewModel().getTimeLive());
        getViewModel().getTimeLive().observe(getViewLifecycleOwner(), (String date) ->{
            binding.time.setText(date);
        });

        position=getViewModel().getEntity().get().getId();

        View view=binding.getRoot();

        return view;
    }
    public void delete(View view){
        Loger.log(position);
        getViewModel().deletePosition(position);
        Navigation.findNavController(view).popBackStack(R.id.remindersFragment3,false);
    }

}