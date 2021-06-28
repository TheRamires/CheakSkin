package ru.skinallergic.checkskin.components.healthdiary.components;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerThemeSmall extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private CalendarListener calendarListener;

    public void setCalendarListener(CalendarListener calendarListener){
        this.calendarListener=calendarListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);

        return datepickerdialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String myMonth = String.format("%02d",month+1, 1);
        String myDay = String.format("%02d", day,1);
        //day of

        String stringDate=day+"."+myMonth+"."+year;
        calendarListener.getDate(stringDate);

    }
    interface CalendarListener{
        void getDate(String date);
    }
}