package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;
import ru.skinallergic.checkskin.databinding.FragmentReminderRedactBinding;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogTheme;

import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.RemindersFragment.BUNDLE_ID_OF_REMIND;

public class RemindersRedactFragment extends BaseRemindersFragment {
    private int position;
    private DialogFragment dialogFragment;
    private Spinner typeSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            position= getArguments().getInt(BUNDLE_ID_OF_REMIND);
        }catch (Throwable ignore){ }
        getViewModel().clearCurrent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentReminderRedactBinding binding=FragmentReminderRedactBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(getViewModel());
        initBackGround(binding.background);
        typeSpinner=binding.type;
        ArrayAdapter<?> adapter =
                new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,getTypeList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(getTypeList().get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

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
        getViewModel().getTimeLive().observe(getViewLifecycleOwner(), (Date time) ->{
            binding.time.setText(getSimpleTimeParser().format(time));
            EntityReminders reminder =getViewModel().getCreatingReminder().getValue();
            reminder.setTime(time);
            getViewModel().getCreatingReminder().setValue(reminder);
        });

        getViewModel().getRemindsLive().observe(getViewLifecycleOwner(), (ArrayList<EntityReminders> list) ->{
            EntityReminders entity=list.get(position);
            getViewModel().getCreatingReminder().setValue(entity);

        });

        getViewModel().getCreatingReminder().observe(getViewLifecycleOwner(),(EntityReminders entityReminders)-> {
            getViewModel().getEntity().set(entityReminders);

        });

        //position=getViewModel().getEntity().get().getId();

        View view=binding.getRoot();

        return view;
    }
    public void delete(View view){
        Loger.log(position);
        getViewModel().deletePosition(position);
        Navigation.findNavController(view).popBackStack(R.id.remindersFragment3,false);
    }

    @Override
    public void backStack(View view){
        System.out.println("save CreatingReminder"+getViewModel().getCreatingReminder());
        Navigation.findNavController(view).popBackStack();
    }
}