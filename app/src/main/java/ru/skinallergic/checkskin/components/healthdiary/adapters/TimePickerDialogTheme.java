package ru.skinallergic.checkskin.components.healthdiary.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerDialogTheme extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    MutableLiveData<Date> timeLive;
    public TimePickerDialogTheme(MutableLiveData timeLive){
        this.timeLive=timeLive;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT,this,hour,minutes,true);

        return timePickerDialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String myHour = String.format("%02d",hourOfDay, 1);
        String myMinutes = String.format("%02d",minute, 1);
        String timeStr=myHour+":"+ myMinutes;

        SimpleDateFormat formater = new SimpleDateFormat("HH:mm");
        try {
            Date time = formater.parse(timeStr);
            timeLive.setValue(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //timeLive.setValue(myHour+":"+ myMinutes);
    }
}