package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderDetailViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderWriterViewModel;
import ru.skinallergic.checkskin.databinding.FragmentRemindersRepeatBinding;

public class RemindersRepeatFragment extends BaseRemindersFragment {
    private FragmentRemindersRepeatBinding binding;
    private ReminderWriterViewModel reminderWriterViewModel;
    private ReminderDetailViewModel reminderDetailViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reminderWriterViewModel=
                new ViewModelProvider(requireActivity(), getViewModelFactory()).get(ReminderWriterViewModel.class);
        reminderDetailViewModel=
                new ViewModelProvider(requireActivity(), getViewModelFactory()).get(ReminderDetailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRemindersRepeatBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        initBackGround(binding.background);

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position =getPosition(checkedId);
/*
                EntityReminders reminder =getViewModel().getCreatingReminder().getValue();
                reminder.setRepeat(position);
                getViewModel().getCreatingReminder().setValue(reminder);*/
                reminderWriterViewModel.setRepeatMode(position);
                reminderDetailViewModel.setRepeatMode(position);
            }
        });
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backStack(v);
            }
        });

        return view;
    }
    private int getPosition(int id){
        int position=0;
        switch (id){
            case R.id.radio_0:
                position=0;
                break;
            case R.id.radio_1:
                position=1;
                break;
            case R.id.radio_2:
                position=2;
                break;
            case R.id.radio_3:
                position=3;
                break;
            case R.id.radio_4:
                position=4;
                break;
            case R.id.radio_5:
                position=5;
                break;
        }
        return position;
    }
}