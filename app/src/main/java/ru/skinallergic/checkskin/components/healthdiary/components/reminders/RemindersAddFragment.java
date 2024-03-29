package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.Date;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogThemeForDoctors;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderWriterViewModel;
import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogTheme;
import ru.skinallergic.checkskin.databinding.FragmentRemindersAddBinding;

import static ru.skinallergic.checkskin.components.healthdiary.components.CalendarFragment.ARE_YOU_DOCTOR;
import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.DATE_PATTERN;
import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.TIME_PATTERN;

public class RemindersAddFragment extends BaseRemindersFragment implements TimePickerDialogTheme.TimeClickListener , TimePickerDialogThemeForDoctors.DoctorTimeClickListener {
    private  TimePickerDialogTheme dialogfragment;
    private  TimePickerDialogThemeForDoctors doctorTimeFragment;
    private FragmentRemindersAddBinding binding;
    private Spinner typeSpinner;
    private ReminderWriterViewModel reminderWriterViewModel;
    private Boolean isDoctor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewModel().clearCurrent();
        reminderWriterViewModel=
                new ViewModelProvider(requireActivity(), getViewModelFactory()).get(ReminderWriterViewModel.class);
        //clearedViewModel--
        reminderWriterViewModel.getReminderWriter().set(null);
        changeOff();

        dialogfragment = new TimePickerDialogTheme();
        dialogfragment.setTimeClickListener(this);
        doctorTimeFragment=new TimePickerDialogThemeForDoctors(getDateViewModel());
        doctorTimeFragment.setTimeClickListener(this);
        getDateViewModel().clearDoctorDateLive();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentRemindersAddBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(reminderWriterViewModel);
        binding.setBaseViewModel(getViewModelCommon());
        initBackGround(binding.background);
        typeSpinner=binding.type;
        System.out.println("-------------------------------"+reminderWriterViewModel.isChanged());

        ArrayAdapter<?> adapter =
                new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,getTypeList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        binding.date.setText(getDateViewModel().getDate(TIME_PATTERN));

        Loger.log("-------------getDateViewModel getDoctorDate "+getDateViewModel().doctorDateLive.getValue());
        binding.dateDoctor.setText(getDateViewModel().getDoctorDate(DATE_PATTERN));

        ReminderWriter reminderWriter=reminderWriterViewModel.getReminderWriter().get();
        if (reminderWriter!=null){
            binding.time.setText(reminderWriter.getTime()); //Почему-то observableField не срабатывает для этого поля
            binding.description.setText(reminderWriter.getText());
        }

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(getTypeList().get(position));
                reminderWriterViewModel.setKind(position);
                if (position==0){
                    isDoctor=true;
                    binding.redactDate.setVisibility(View.VISIBLE);
                } else if (position==1){
                    isDoctor=false;
                    binding.redactDate.setVisibility(View.GONE);
                }
                //changeOn();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        binding.redactTime.setOnClickListener((View v)-> {
            if (isDoctor){
                doctorTimeFragment.show(getParentFragmentManager(),"time");
            }else {
                dialogfragment.show(getParentFragmentManager(),"time");
            }

        });
        binding.redactRemind.setOnClickListener((View v)-> {
            Navigation.findNavController(v).navigate(R.id.remindersPeriodFragment);

        });
        binding.redactRepeat.setOnClickListener((View v)-> {
            Navigation.findNavController(v).navigate(R.id.remindersRepeatFragment);

        });
        binding.redactDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putBoolean(ARE_YOU_DOCTOR, true);
                Navigation.findNavController(v).navigate(R.id.calendarFragment, bundle);
            }
        });

        getDateViewModel().dateLive.observe(getViewLifecycleOwner(),(Date d)-> {
            String date=getDateViewModel().getDate(d);
            binding.date.setText(date);
        });

        getViewModel().getEntity().get();

        binding.addButton.setOnClickListener((View v)-> {
            add();
            changeOff();
        });

        View view=binding.getRoot();

        getViewModelCommon().getSaveComplite().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Loger.log("getSaveComplite "+aBoolean);
                if (aBoolean){
                    getViewModelCommon().getSaveComplite().setValue(false);
                    Navigation.findNavController(view).popBackStack();

                }
            }
        });

        System.out.println("-------------------------------"+reminderWriterViewModel.isChanged());
        return view;
    }
    @Override
    public void backStack(View view){
        System.out.println("save CreatingReminder"+getViewModel().getCreatingReminder());
        //Navigation.findNavController(view).popBackStack();

        quitSaveLogic(new Function0<Boolean>() {
            @Override
            public Boolean invoke() {
                //return reminderWriterViewModel.conditionQuitSave();
                return reminderWriterViewModel.isChanged();
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                Navigation.findNavController(view).popBackStack();
                return null;
            }
        }, new Function0<Unit>() {
            @Override
            public Unit invoke() {
                add();
                return null;
            }
        });
    }

    public void add(){

        //************************* специально для визита к врачу
        Date time=getDateViewModel().doctorTimeLive.getValue();

        long millisInDay = 60 * 60 * 24 * 1000;
        long currentTime = getDateViewModel().doctorDateLive.getValue().getTime();

        long dateOnly = (currentTime / millisInDay) * millisInDay;
        Date clearDate = new Date(dateOnly);

        if (isDoctor){
            reminderWriterViewModel.setStartAt(
                    clearDate.getTime()+time.getTime()
            );
        }
        //**************

        System.out.println(reminderWriterViewModel.getReminderWriter().get());
        ReminderWriter reminderWriter=reminderWriterViewModel.getEntity();
        if (reminderWriter!=null){
            getViewModelCommon().newRemind(reminderWriter);
        }
    }
    public void nameChanged(CharSequence s,int start, int count, int after){
        System.out.println("after "+after);
        reminderWriterViewModel.setText(s.toString());
        /*if (s.length()>3){
            reminderWriterViewModel.setText(s.toString());
        }*/
        changeOn();
    }

    @Override
    public void timeClick(Long time) {
        Loger.log("timeClick "+time);
        reminderWriterViewModel.setStartAt(time);
        System.out.println("1 "+ reminderWriterViewModel.getReminderWriter().get());
        binding.time.setText(reminderWriterViewModel.getReminderWriter().get().getTime()); //Почему-то observableField не срабатывает для этого поля
        changeOn();
    }
    @Override
    public void doctorTimeClick(Long time) {
        Loger.log("timeClick "+time);
        reminderWriterViewModel.setStartAt(time);
        System.out.println("1 "+ reminderWriterViewModel.getReminderWriter().get());
        binding.time.setText(reminderWriterViewModel.getReminderWriter().get().getTime()); //Почему-то observableField не срабатывает для этого поля
        changeOn();
    }

    public void changeOn(){
        reminderWriterViewModel.changedOn();
    }
    public void changeOff(){
        reminderWriterViewModel.changedOff();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        changeOff();
    }

}