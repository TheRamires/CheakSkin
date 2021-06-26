package ru.skinallergic.checkskin.components.fooddiary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerTheme extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private MutableLiveData<String> datelive;
    public DatePickerTheme (MutableLiveData<String> datelive){
        this.datelive=datelive;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year,month,day);

        return datepickerdialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String myMonth = String.format("%02d",month+1, 1);
        String myDay = String.format("%02d", day,1);
        //day of

        String stringDate=day+"/"+myMonth+"/"+year;
        String string=getDayOfWeak(stringDate)+", "+myDay+"."+myMonth+"."+year;

        datelive.setValue((string));

    }

    private String getDayOfWeak(String stringDate){
        String day = null;
        try {
            Date date=new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            switch (dayOfWeek){
                case 1:
                    day="воскресенье";
                    break;
                case 2:
                    day="понедельник";
                    break;
                case 3:
                    day="вторник";
                    break;
                case 4:
                    day="среда";
                    break;
                case 5:
                    day="четверг";
                    break;
                case 6:
                    day="пятница";
                    break;
                case 7:
                    day="суббота";
                    break;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return day;
    }

}
