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
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderWriterViewModel;
import ru.skinallergic.checkskin.databinding.FragmentRemindersPeriodBinding;

import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.REMIND_IN_AN_30_MIN;
import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.REMIND_IN_AN_5_MIN;
import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.REMIND_IN_AN_HOUR;
import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.REMIND_IN_AN_NEVER;

public class RemindersPeriodFragment extends BaseRemindersFragment {
    private FragmentRemindersPeriodBinding binding;
    private ReminderWriterViewModel reminderWriterViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reminderWriterViewModel=
                new ViewModelProvider(requireActivity(), getViewModelFactory()).get(ReminderWriterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRemindersPeriodBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        initBackGround(binding.background);

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int position=getPosition(checkedId);

                reminderWriterViewModel.setRemind(position);

                /*EntityReminders reminder =getViewModel().getCreatingReminder().getValue();
                reminder.setRemind(position);
                getViewModel().getCreatingReminder().setValue(reminder);*/
            }
        });

        return view;
    }
    private int getPosition(int id){
        int position=0;
        switch (id){
            case R.id.radio_0:
                position=REMIND_IN_AN_NEVER;
                break;
            case R.id.radio_1:
                position=REMIND_IN_AN_5_MIN;
                break;
            case R.id.radio_2:
                position=REMIND_IN_AN_30_MIN;
                break;
            case R.id.radio_3:
                position=REMIND_IN_AN_HOUR;
                break;
        }
        return position;
    }
}