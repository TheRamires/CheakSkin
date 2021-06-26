package ru.skinallergic.checkskin.components.healthdiary;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Date;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;

import ru.skinallergic.checkskin.databinding.FragmentHealthDiaryBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.components.fooddiary.DatePickerTheme;

public class HealthDiaryFragment extends Fragment {
    private Bundle bundle;
    private DialogFragment dialogfragment;
    public ObservableField<String> dateObservable=new ObservableField<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FragmentHealthDiaryBinding binding= FragmentHealthDiaryBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        bundle=new Bundle();

        //Настройка кнопок ---------------------------------------------------------------------

        AppCompatButton btnState = binding.buttonState;
        Drawable icon1 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_1);
        Drawable cheakBox1 = ContextCompat.getDrawable(getActivity(), R.drawable.ic_cheakbox_blue_off);
        icon1.setBounds(0, 0, 75, 75);
        cheakBox1.setBounds(0, 0, 45, 45);
        btnState.setCompoundDrawables(icon1, null, cheakBox1, null);

        AppCompatButton btnPhoto = binding.buttonPhoto;
        Drawable icon2 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_2);
        Drawable cheakBox2 = ContextCompat.getDrawable(getActivity(), R.drawable.ic_cheakbox_blue_off);
        icon2.setBounds(0, 0, 75, 75);
        cheakBox2.setBounds(0, 0, 45, 45);
        btnPhoto.setCompoundDrawables(icon2, null, cheakBox2, null);

        AppCompatButton btnTriggers = binding.buttonTriggers;
        Drawable icon3 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_3);
        Drawable cheakBox3 = ContextCompat.getDrawable(getActivity(), R.drawable.ic_cheakbox_blue_off);
        icon3.setBounds(0, 0, 75, 75);
        cheakBox3.setBounds(0, 0, 45, 45);
        btnTriggers.setCompoundDrawables(icon3, null, cheakBox3, null);

        AppCompatButton btnHealth = binding.buttonHelth;
        Drawable icon4 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_4);
        Drawable cheakBox4 = ContextCompat.getDrawable(getActivity(), R.drawable.ic_cheakbox_blue_off);
        icon4.setBounds(0, 0, 75, 75);
        cheakBox4.setBounds(0, 0, 45, 45);
        btnHealth.setCompoundDrawables(icon4, null, cheakBox4, null);

        AppCompatButton btnRemind = binding.buttonRemind;
        Drawable icon5 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_5);
        Drawable cheakBox5 = ContextCompat.getDrawable(getActivity(), R.drawable.ic_cheakbox_blue_off);
        icon5.setBounds(0, 0, 75, 75);
        cheakBox5.setBounds(0, 0, 45, 45);
        btnRemind.setCompoundDrawables(icon5, null, cheakBox5, null);

        AppCompatButton btnRate = binding.buttonRate;
        Drawable icon6 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_6);
        Drawable cheakBox6 = ContextCompat.getDrawable(getActivity(), R.drawable.ic_cheakbox_blue_off);
        icon6.setBounds(0, 0, 75, 75);
        cheakBox6.setBounds(0, 0, 45, 45);
        btnRate.setCompoundDrawables(icon6, null, cheakBox6, null);

        //--------------------------------------------------------------------------------------
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        DateViewModel dateViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel.class);
        dateObservable.set(dateViewModel.getDate());

        //dialogfragment = new DatePickerTheme(dateViewModel.dateLive);

        dateViewModel.dateLive.observe(getViewLifecycleOwner(), (Date d)-> {
            String date=dateViewModel.getDate(d);
            bundle.putString("date",date);
            dateObservable.set(date);
        });
        return view;
    }
    public void clickDate(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_calendarFragment);
    }
    public void statistics(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_statisticsFragment);
    }

    public void toState(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_stateFragment);
    }

    public void toPhoto(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_affectedAreasFragment);
    }

    public void toTriggers(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_triggersFragment);
    }

    public void toTreatment (View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_treatmentFragment);
    }

    public void toReminders(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_remindersFragment3,bundle);
    }
    
    public void toRate (View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_ratingFragment);
    }
}