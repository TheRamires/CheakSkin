package ru.skinallergic.checkskin.components.healthdiary.components;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Date;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaCommonViewModel;
import ru.skinallergic.checkskin.databinding.FragmentCalendarBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.handlers.ToastyManager;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import static ru.skinallergic.checkskin.view_models.DateViewModel.DAY_FORMAT;
import static ru.skinallergic.checkskin.view_models.DateViewModel.MONTH_FORMAT;
import static ru.skinallergic.checkskin.view_models.DateViewModel.YEAR_FORMAT;

public class CalendarFragment extends Fragment {
    private DateViewModel dateViewModel;
    private DatePicker datePicker;
    private AffectedAreaCommonViewModel commonViewModel;
    private ToastyManager toastyManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory = App.getInstance().getAppComponent().getViewModelFactory();
        dateViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel.class);
        commonViewModel = new ViewModelProvider(requireActivity(),viewModelFactory).get(AffectedAreaCommonViewModel.class);
        toastyManager=App.getInstance().getAppComponent().getToastyManager();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Loger.log("destroyView by CalendarFrag");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCalendarBinding binding=FragmentCalendarBinding.inflate(inflater);
        View view=binding.getRoot();

        binding.setFragment(this);
        datePicker=binding.datePicker;

        int day=Integer.parseInt(dateViewModel.getDate(DAY_FORMAT));
        int month=Integer.parseInt(dateViewModel.getDate(MONTH_FORMAT))-1;
        int year=Integer.parseInt(dateViewModel.getDate(YEAR_FORMAT));
        datePicker.updateDate(year,month,day);

        if (Build.VERSION.SDK_INT>=26){
            Loger.log("version "+Build.VERSION.SDK_INT);
            binding.acceptButton.setVisibility(View.GONE);

            datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    String month=String.format("%02d",monthOfYear+1);
                    String day=String.format("%02d",dayOfMonth);

                    //**********************************часовыой пояс
                    //String dateString=day+"."+month+"."+year;
                    //Date date=dateViewModel.simpleFormattingToDate(dateString);

                    String dateString="12:00 "+day+"."+month+"."+year;  //stump
                    Date date=dateViewModel.simpleFormattingToDateStump(dateString); //stump
                    setDateLive(date);
                    //**********************************часовыой пояс

                    Loger.log("unix "+dateViewModel.getDateUnix());
                    Loger.log("calendar "+dateViewModel.getDate());
                    commonViewModel.clearMaps();

                    backStack(view);
                }
            });
        }

        return view;
    }

/*
    @Override
    public void onPause() {
        super.onPause();
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
        commonViewModel.clearMaps();
    }
*/
    public void backStack (View view){Navigation.findNavController(view).popBackStack();}

    public String getDate(){
        int monthOfYear=datePicker.getMonth();
        int dayOfMonth=datePicker.getDayOfMonth();
        int year=datePicker.getYear();

        String month=String.format("%02d",monthOfYear+1);
        String day=String.format("%02d",dayOfMonth);

//**********************************часовыой пояс
        //String dateString=day+"."+month+"."+year;
        //Date date=dateViewModel.simpleFormattingToDate(dateString);

        String dateString="12:00 "+day+"."+month+"."+year;  //stump
        Date date=dateViewModel.simpleFormattingToDateStump(dateString); //stump
//**********************************часовыой пояс

        setDateLive(date);

        Loger.log("unix "+dateViewModel.getDateUnix());
        Loger.log("calendar "+dateViewModel.getDate());
        commonViewModel.clearMaps();
        return dateString;
    }
    public void toAccept(View view){
        getDate();
        backStack(view);
    }
    private void setDateLive(Date date){
        Date realCurrent=new Date();
        if (date.getTime()>realCurrent.getTime()){
            dateViewModel.dateLive.setValue(realCurrent);
            toastyManager.toastyyyy("Дата не может быть позднее текущей");
        } else dateViewModel.dateLive.setValue(date);
    }
}
