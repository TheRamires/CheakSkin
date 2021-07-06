package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.annotation.Nullable;
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
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindAddViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindersViewModel;
import ru.skinallergic.checkskin.databinding.FragmentRemindersAddBinding;

import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogTheme;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;

public class RemindersAddFragment extends BaseRemindersFragment {
    private  DialogFragment dialogfragment;
    private FragmentRemindersAddBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().clearCurrent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentRemindersAddBinding.inflate(inflater);
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
        getViewModel().getTimeLive().observe(getViewLifecycleOwner(), (Date time) ->{
            binding.time.setText(getSimpleTimeParser().format(time));
            EntityReminders reminder =getViewModel().getCreatingReminder().getValue();
            reminder.setTime(time);
            getViewModel().getCreatingReminder().setValue(reminder);
            getViewModel().getEntity().set(reminder);
        });


        /*
        viewModel.getTimeLive().observe(getViewLifecycleOwner(), (String date) ->{
            binding.timeAdd.setText(date);
        });*/

        View view=binding.getRoot();
        return view;
    }
    @Override
    public void backStack(View view){
        System.out.println("save CreatingReminder"+getViewModel().getCreatingReminder());
        Navigation.findNavController(view).popBackStack();
    }
}