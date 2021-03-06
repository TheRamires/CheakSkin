package ru.skinallergic.checkskin.components.healthdiary.components;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import static ru.skinallergic.checkskin.view_models.DateViewModel.DATE_NORMAL;

public class DatePickerThemeSmall extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private CalendarListener calendarListener;
    private Date startDate=null;
    private Boolean isEndDate;

    public void setCalendarListener(CalendarListener calendarListener){
        this.calendarListener=calendarListener;
    }

    public  void  setStartDate (Date startDate){
        if (startDate!=null){
            this.startDate=startDate;
        }
    }
    public DatePickerThemeSmall (Date startDate, Boolean isEndDate){
        this.isEndDate=isEndDate;
        this.startDate=startDate;

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
    public void onDateSet(DatePicker view, int year, int month, int day_) {
        String myMonth = String.format("%02d",month+1, 1);
        //String myDay = String.format("%02d", day,1);
        //day of
        int day=day_;

        Date formattedDate= DateViewModel.simpleFormattingToDateWithMin("23:59 "+String.format("%02d", day)+"."+myMonth+"."+year);
        Date current=new Date();
        if (formattedDate.getTime()>current.getTime()){
            Toast.makeText(requireContext(),"Дата не может быть больше текущей",Toast.LENGTH_SHORT).show();
            Loger.log("Дата не может быть больше текущей");
            String stringDate= DateViewModel.formating(current,DATE_NORMAL);
            calendarListener.getDate(stringDate);
            return;

        }

        //Если dataEnd меньше startData, то увеличиваем день на один
        if (isEndDate){
            if (startDate!=null && formattedDate.compareTo(startDate)<=0){
                Toast.makeText(requireContext(),"Конечная дата не может быть раньше начальной",Toast.LENGTH_SHORT).show();
                Loger.log("Конечная дата не может быть раньше начальной");

                Calendar myCal = new GregorianCalendar();
                myCal.setTime(startDate);
                myCal.add(Calendar.DAY_OF_MONTH, 1);

                String stringDate= DateViewModel.formating(myCal.getTime(),DATE_NORMAL);
                calendarListener.getDate(stringDate);
                return;
            }
        }

        String stringDate=String.format("%02d", day)+"."+myMonth+"."+year;
        calendarListener.getDate(stringDate);
    }
    interface CalendarListener{
        void getDate(String date);
    }
}