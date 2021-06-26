package ru.skinallergic.checkskin.components.healthdiary.components;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.StateViewModel;
import ru.skinallergic.checkskin.databinding.FragmentCalendarBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;

public class CalendarFragment extends Fragment {
    private DateViewModel dateViewModel;
    private DatePicker datePicker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory = App.getInstance().getAppComponent().getViewModelFactory();
        dateViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        int monthOfYear=datePicker.getMonth();
        int dayOfMonth=datePicker.getDayOfMonth();
        int year=datePicker.getYear();


        String month=String.format("%02d",monthOfYear+1);
        String day=String.format("%02d",dayOfMonth);
        String dateString=day+"."+month+"."+year;

        Date date=dateViewModel.simpleFormattingToDate(dateString);
        dateViewModel.dateLive.setValue(date);
        Loger.log("unix "+dateViewModel.getDateUnix());
        Loger.log("calendar "+dateViewModel.getDate());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCalendarBinding binding=FragmentCalendarBinding.inflate(inflater);
        View view=binding.getRoot();

        binding.setFragment(this);
        datePicker=binding.datePicker;
/*
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //"dd.MM.yyyy"
                String month=String.format("%02d",monthOfYear+1);
                String day=String.format("%02d",dayOfMonth);
                String dateString=day+"."+month+"."+year;

                Date date=dateViewModel.simpleFormattingToDate(dateString);
                dateViewModel.dateLive.setValue(date);
                Loger.log("unix "+dateViewModel.getDateUnix());
                Loger.log("calendar "+dateViewModel.getDate());

                //проверка
/*
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
                String ttt=formatter.format(dateViewModel.getDateUnix()*1000);

                //---------------------------
                dateViewModel.setCurrentDate();
                Loger.log("unix current "+dateViewModel.getDateUnix());

                //------------------------------

                Loger.log("new date "+ttt);
          //  }
       // });
*/
        return view;
    }
    public void backStack (View view){
        Navigation.findNavController(view).popBackStack();
    }
}