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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogThemeForDoctors;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderEntity;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderDetailViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderWriterViewModel;
import ru.skinallergic.checkskin.databinding.FragmentReminderRedactBinding;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogTheme;

import static ru.skinallergic.checkskin.components.fooddiary.components.DetailFoodFragmentKt.BUNDLE_ID_OF_REMIND;
import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.DATE_PATTERN;


public class RemindersRedactFragment extends BaseRemindersFragment implements TimePickerDialogTheme.TimeClickListener, TimePickerDialogThemeForDoctors.DoctorTimeClickListener {
    private int positionId;
    private TimePickerDialogTheme dialogFragment;
    private  TimePickerDialogThemeForDoctors doctorTimeFragment;
    private Spinner typeSpinner;
    private ReminderDetailViewModel reminderDetailViewModel;
    private ReminderWriterViewModel reminderWriterViewModel;
    private FragmentReminderRedactBinding binding;
    private Boolean isDoctor;

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
        doctorTimeFragment=new TimePickerDialogThemeForDoctors(getDateViewModel());
        doctorTimeFragment.setTimeClickListener(this);
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

        ReminderEntity reminderEntity=reminderDetailViewModel.getReminderDetail().get();
        Loger.log("reminderEntity \n\n\n\n\n "+reminderEntity);
        typeSpinner.setSelection(reminderEntity.getKind());
        binding.dateDoctor.setText(new SimpleDateFormat(DATE_PATTERN).format(reminderEntity.getStart_at()*1000));
        getDateViewModel().doctorDateLive.setValue(new Date(reminderEntity.getStart_at()));

        reminderWriterViewModel.setStartAtForHorror(reminderEntity.getStart_at());
        reminderWriterViewModel.setRepeatMode(reminderEntity.getRepeat_mode());
        reminderWriterViewModel.setText(reminderEntity.getText());
        reminderWriterViewModel.setRemind(reminderEntity.getRemind());
        reminderWriterViewModel.setKind(reminderEntity.getKind());

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(getTypeList().get(position));
                if (position==0){
                    isDoctor=true;
                    binding.redactDate.setVisibility(View.VISIBLE);
                } else if (position==1){
                    isDoctor=false;
                    binding.redactDate.setVisibility(View.GONE);
                }
                reminderWriterViewModel.setKind(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        binding.redactTime.setOnClickListener((View v)-> {
            if (isDoctor){
                doctorTimeFragment.show(getParentFragmentManager(),"time");
            }else {
                dialogFragment.show(getParentFragmentManager(),"time");
            }


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

                horror();

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
    @Override
    public void doctorTimeClick(Long time) {
        reminderWriterViewModel.setStartAt(time);
        System.out.println("1 "+ reminderWriterViewModel.getReminderWriter().get());
        binding.time.setText(reminderWriterViewModel.getReminderWriter().get().getTime()); //Почему-то observableField не срабатывает для этого поля
        changeOn();

    }
    private void horror() {
        //************************* специально для визита к врачу

        if (isDoctor) {
            Date time = getDateViewModel().doctorTimeLive.getValue();
            if (time==null){
                return; //значит изменений не было
            }

            ReminderEntity reminderEntity=reminderDetailViewModel.getReminderDetail().get();
            Loger.log("reminderWriterViewModel.getEntity()\n\n ------------"+reminderEntity.getStart_at());
            long millisInDay = 60 * 60 * 24 ;
            long currentTime = reminderEntity.getStart_at();

            long dateOnly = (currentTime / millisInDay) * millisInDay;
            Date clearDate = new Date(dateOnly);
            Loger.log("clearDate.getTime() \n\n ------------"+clearDate.getTime());
            Loger.log("time.getTime() \n\n ------------"+time.getTime()/1000);
            reminderWriterViewModel.setStartAtForHorror(
                    (clearDate.getTime()  + time.getTime()/1000)
            );
        }
        //**************
    }


    public void redact(){
        horror();

        System.out.println(reminderWriterViewModel.getReminderWriter().get());
        ReminderWriter reminderWriter=reminderWriterViewModel.getEntity();
        Loger.log("------------------------ \n reminderWriter \n "+reminderWriter);
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