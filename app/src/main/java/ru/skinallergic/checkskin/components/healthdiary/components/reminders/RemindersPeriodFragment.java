package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;
import ru.skinallergic.checkskin.databinding.FragmentRemindersPeriodBinding;

public class RemindersPeriodFragment extends BaseRemindersFragment {
    private FragmentRemindersPeriodBinding binding;

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

                EntityReminders reminder =getViewModel().getCreatingReminder().getValue();
                reminder.setRemind(position);
                getViewModel().getCreatingReminder().setValue(reminder);
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
        }
        return position;
    }
}