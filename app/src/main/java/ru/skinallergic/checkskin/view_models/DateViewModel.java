package ru.skinallergic.checkskin.view_models;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import javax.inject.Inject;

import ru.skinallergic.checkskin.Loger;

public class DateViewModel extends ViewModel {
    public static String DAY_FORMAT="d";
    public static String MONTH_FORMAT="M";
    public static String YEAR_FORMAT="yyyy";
    public static String DATE_NORMAL="dd.MM.yyyy";

    @Inject
    public DateViewModel (){}
    //public MutableLiveData<String> dateLive=new MutableLiveData();
    public MutableLiveData<Date> dateLive=new MutableLiveData();
    public MutableLiveData<Date> doctorDateLive=new MutableLiveData();
    public MutableLiveData<Date> doctorTimeLive=new MutableLiveData();
    public ObservableField<Boolean> barVisibility=new ObservableField<>(true);

    public String getDate(Date date){
        return formating(date);
    }

    public String getDate(){
        if (dateLive.getValue()==null) {
            setCurrentDate();
        }
        return formating(dateLive.getValue());
    }
    public String getDate(String format){
        if (dateLive.getValue()==null) {
            setCurrentDate();
        }
        return formating(dateLive.getValue(),format);
    }
    public String getDoctorDate(String format){
        if (dateLive.getValue()==null) {
            setCurrentDate();
        }
        return formating(doctorDateLive.getValue(),format);
    }


    public String getDate(SimpleDateFormat sdf){
        if (dateLive.getValue()==null) {
            setCurrentDate();
        }
        return sdf.format(dateLive.getValue());
    }

    public void setCurrentDate(){
        Loger.log("получить текущую дату");
        Date currentDate = new Date();
        dateLive.setValue(currentDate);

    }
    public Long getDateUnix(){
        if (dateLive.getValue()==null) {
            setCurrentDate();
        }
        return Objects.requireNonNull(dateLive.getValue()).getTime();
    }

    public String formating(Date date){

        DateFormatSymbols myDateFormatSymbols = new DateFormatSymbols() {
            @Override
            public String[] getWeekdays() {
                return new String[] {"", "Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
            }
        };

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd.MM.yyyy", myDateFormatSymbols);
        return sdf.format(date);
    }
    public static String formating(Date date, String Pattern){

        SimpleDateFormat sdf = new SimpleDateFormat(Pattern,Locale.getDefault());
        return sdf.format(date);
    }

    //**********************************часовыой пояс
    public Date simpleFormattingToDateStump(String formatedStringDate){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy",Locale.getDefault());
        Date date = null;
        try {
            date = formatter.parse(formatedStringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long unixTime = (long)date.getTime()/1000;

        return date;
    }
    //**********************************часовыой пояс

    public Date simpleFormattingToDate(String formatedStringDate){
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy",Locale.getDefault());
        Date date = null;
        try {
            date = formatter.parse(formatedStringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long unixTime = (long)date.getTime()/1000;

        return date;
    }
    public Date simpleFormattingToDate(String formatedStringDate, String pattern){
        SimpleDateFormat formatter = new SimpleDateFormat(pattern,Locale.getDefault());
        Date date = null;
        try {
            date = formatter.parse(formatedStringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
    public static Date simpleFormattingToDateWithMin(String formatedStringDate){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy",Locale.getDefault());
        Date date = null;
        try {
            date = formatter.parse(formatedStringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long unixTime = (long)date.getTime()/1000;

        return date;
    }
/*
    public long getTZOffset(){
        //time zone offset in seconds
        return (long) (TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 1000);
    }
    public long getUnixWithOffset(){
        long offsetInSecond=getTZOffset();
        long currentTimeUnix=getDateUnix()/1000;
        return currentTimeUnix+offsetInSecond;
    }*/

    public void clearDoctorDateLive(){
        doctorDateLive.setValue(dateLive.getValue());
    }
}

