package ru.skinallergic.checkskin.components.healthdiary.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.skinallergic.checkskin.view_models.DateViewModel;

public class TimePickerDialogThemeForDoctors extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
    public DoctorTimeClickListener timeClickListener;
    private DateViewModel dateViewModel;
    public TimePickerDialogThemeForDoctors(DateViewModel dateViewModel){
        this.dateViewModel=dateViewModel;

    }
    public void setTimeClickListener(DoctorTimeClickListener timeClickListener){
        this.timeClickListener=timeClickListener;
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
            dateViewModel.doctorTimeLive.setValue(time);

            long millisInDay = 60 * 60 * 24 * 1000;
            long currentTime = dateViewModel.doctorDateLive.getValue().getTime();

            long dateOnly = (currentTime / millisInDay) * millisInDay;
            Date clearDate = new Date(dateOnly);

            timeClickListener.doctorTimeClick(clearDate.getTime()+time.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("2 "+e);
        }

    }
    public interface DoctorTimeClickListener {
        public void doctorTimeClick(Long time);
    }
}