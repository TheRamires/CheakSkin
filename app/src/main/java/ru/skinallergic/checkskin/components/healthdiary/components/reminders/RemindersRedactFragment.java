package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderEntity;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderDetailViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderWriterViewModel;
import ru.skinallergic.checkskin.databinding.FragmentReminderRedactBinding;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogTheme;

import static ru.skinallergic.checkskin.components.fooddiary.components.DetailFoodFragmentKt.BUNDLE_ID_OF_REMIND;


public class RemindersRedactFragment extends BaseRemindersFragment implements TimePickerDialogTheme.TimeClickListener {
    private int positionId;
    private TimePickerDialogTheme dialogFragment;
    private Spinner typeSpinner;
    private ReminderDetailViewModel reminderDetailViewModel;
    private ReminderWriterViewModel reminderWriterViewModel;
    private FragmentReminderRedactBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reminderDetailViewModel=new ViewModelProvider(requireActivity(),getViewModelFactory()).get(ReminderDetailViewModel.class);
        reminderWriterViewModel=new ViewModelProvider(requireActivity(),getViewModelFactory()).get(ReminderWriterViewModel.class);
        try {
            positionId= getArguments().getInt(BUNDLE_ID_OF_REMIND);
        }catch (Throwable ignore){ }
        //getViewModel().clearCurrent();
        dialogFragment = new TimePickerDialogTheme();
        dialogFragment.setTimeClickListener(this);
        changeOff();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentReminderRedactBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(reminderDetailViewModel);
        binding.setBaseViewModel(getViewModelCommon());
        initBackGround(binding.background);
        typeSpinner=binding.type;
        ArrayAdapter<?> adapter =
                new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,getTypeList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        typeSpinner.setSelection(reminderDetailViewModel.getReminderDetail().get().getKind());
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

       /*
        getViewModel().getTimeLive().observe(getViewLifecycleOwner(), (Date time) ->{
            binding.time.setText(getSimpleTimeParser().format(time));
            EntityReminders reminder =getViewModel().getCreatingReminder().getValue();
            reminder.setTime(time);
            getViewModel().getCreatingReminder().setValue(reminder);
        });
*/
   /*     getViewModelCommon().getRemindLive().observe(getViewLifecycleOwner(), new Observer<List<ReminderEntity>>() {
            @Override
            public void onChanged(List<ReminderEntity> list) {
                if (list != null) {
                    for (ReminderEntity entity : list){
                        if(entity.getId()==positionId){
                            reminderDetailViewModel.getReminderDetail().set(entity);
                            typeSpinner.setSelection(entity.getKind());
                        }
                    }
                }
            }
        });
*/
        getViewModel().getCreatingReminder().observe(getViewLifecycleOwner(),(EntityReminders entityReminders)-> {
            getViewModel().getEntity().set(entityReminders);

        });

        View view=binding.getRoot();

        getViewModelCommon().getRedactComplete().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    getViewModelCommon().getRedactComplete().setValue(false);
                    pop(view);
                }
            }
        });

        binding.addButton.setOnClickListener((View v)-> {
            redact();
        });

        return view;
    }
    public void delete(View view){
        getViewModelCommon().deleteRemind(positionId,getDateViewModel().getDateUnix()).observe(getViewLifecycleOwner(),(Boolean aBoolean)-> {
            if (aBoolean){
                getViewModelCommon().getDeletingComplete().setValue(false);
                Navigation.findNavController(view).popBackStack(R.id.remindersFragment3,false);
            }
        });
    }

    public void nameChanged(CharSequence s,int start, int count, int after){
        System.out.println("after "+after);
        reminderWriterViewModel.setText(s.toString());
        reminderDetailViewModel.setText(s.toString());
        changeOn();
        /*if (s.length()>3){
            reminderWriterViewModel.setText(s.toString());
            reminderDetailViewModel.setText(s.toString());
            changeOn();
        } else if (after==0){
            reminderWriterViewModel.setText(s.toString());
            reminderDetailViewModel.setText(s.toString());
            changeOn();
        }*/
    }
    @Override
    public void backStack(View view){
        System.out.println("save CreatingReminder"+getViewModel().getCreatingReminder());
        //pop(view);

        quitSaveLogic(new Function0<Boolean>() {
            @Override
            public Boolean invoke() {
                //return reminderWriterViewModel.conditionQuitSave();
                return reminderWriterViewModel.isChanged();
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                pop(view);
                return null;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                getViewModelCommon().redactRemind(
                        positionId,
                        Objects.requireNonNull(reminderWriterViewModel.getReminderWriter().get())
                );
                return null;
            }
        });

    }
    private void pop(View view){
        Navigation.findNavController(view).popBackStack(R.id.remindersFragment3, false);

    }

    @Override
    public void timeClick(Long time) {
        reminderWriterViewModel.setStartAt(time);
        System.out.println("1 "+ reminderWriterViewModel.getReminderWriter().get());
        binding.time.setText(reminderWriterViewModel.getReminderWriter().get().getTime()); //Почему-то observableField не срабатывает для этого поля
        changeOn();
    }
    public void redact(){
        System.out.println(reminderWriterViewModel.getReminderWriter().get());
        ReminderWriter reminderWriter=reminderWriterViewModel.getEntity();
        ReminderEntity entity=reminderDetailViewModel.getReminderDetail().get();
        if (reminderWriter!=null && entity!=null){
            getViewModelCommon().redactRemind(entity.getId(),reminderWriter);
        }
    }
    public void changeOn(){
        reminderWriterViewModel.changedOn();
    }
    public void changeOff(){
        reminderWriterViewModel.changedOn();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        changeOff();
    }
}