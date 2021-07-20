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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.Date;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderWriterViewModel;
import ru.skinallergic.checkskin.databinding.FragmentRemindersAddBinding;
import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogTheme;

import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.TIME_PATTERN;

public class RemindersAddFragment extends BaseRemindersFragment {
    private  DialogFragment dialogfragment;
    private FragmentRemindersAddBinding binding;
    private Spinner typeSpinner;
    private ReminderWriterViewModel reminderWriterViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().clearCurrent();
        reminderWriterViewModel=
                new ViewModelProvider(requireActivity(), getViewModelFactory()).get(ReminderWriterViewModel.class);
        dialogfragment = new TimePickerDialogTheme(reminderWriterViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentRemindersAddBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(reminderWriterViewModel);
        initBackGround(binding.background);
        typeSpinner=binding.type;
        ArrayAdapter<?> adapter =
                new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,getTypeList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        binding.date.setText(getDateViewModel().getDate(TIME_PATTERN));

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(getTypeList().get(position));
                reminderWriterViewModel.setKind(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

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

       /* getViewModel().getTimeLive().observe(getViewLifecycleOwner(), (Date time) ->{
            //binding.time.setText(getSimpleTimeParser().format(time));

            EntityReminders reminder =getViewModel().getCreatingReminder().getValue();
            reminder.setTime(time);
            getViewModel().getCreatingReminder().setValue(reminder);
            getViewModel().getEntity().set(reminder);

            System.out.println(time.getTime());
            reminderWriterViewModel.setStartAt(time.getTime());
        });*/
        getViewModel().getEntity().get();

        /*
        viewModel.getTimeLive().observe(getViewLifecycleOwner(), (String date) ->{
            binding.timeAdd.setText(date);
        });*/

        binding.add.setOnClickListener((View v)-> {
            add();
        });

        View view=binding.getRoot();
        return view;
    }
    @Override
    public void backStack(View view){
        System.out.println("save CreatingReminder"+getViewModel().getCreatingReminder());
        Navigation.findNavController(view).popBackStack();
    }

    public void add(){
        System.out.println(reminderWriterViewModel.getReminderWriter().get());
        ReminderWriter reminderWriter=reminderWriterViewModel.getEntity();
        if (reminderWriter!=null){
            getViewModelCommon().newRemind(reminderWriter);
        }
    }
    public void nameChanged(CharSequence s,int start, int count, int after){
        System.out.println("after "+after);
        if (s.length()>3){
            reminderWriterViewModel.setText(s.toString());
        }
    }
}