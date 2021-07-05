package ru.skinallergic.checkskin.components.healthdiary;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableField;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Date;
import java.util.List;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;

import ru.skinallergic.checkskin.components.healthdiary.remote.Rash;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.HealthyDiaryViewModel;
import ru.skinallergic.checkskin.databinding.FragmentHealthDiaryBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.components.fooddiary.DatePickerTheme;

public class HealthDiaryFragment extends Fragment {
    public static String TAG_HEALTHY ="healthyDiary";
    private Bundle bundle;
    private DialogFragment dialogfragment;
    public ObservableField<String> dateObservable=new ObservableField<>();
    private DateViewModel dateViewModel;
    private HealthyDiaryViewModel viewModel;
    private Drawable checkMark;
    private Drawable checkMarkOff;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkMark=AppCompatResources.getDrawable(requireContext(),R.drawable.ic_cheakbox_blue_on);
        checkMarkOff=AppCompatResources.getDrawable(requireContext(),R.drawable.ic_cheakbox_blue_off);
        checkMark.setBounds(0, 0, 45, 45);
        checkMarkOff.setBounds(0, 0, 45, 45);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getSum().set(0);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FragmentHealthDiaryBinding binding= FragmentHealthDiaryBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        bundle=new Bundle();


        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        dateViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel.class);
        viewModel=new ViewModelProvider(this,viewModelFactory).get(HealthyDiaryViewModel.class);

        Loger.log(dateViewModel.getDateUnix(),TAG_HEALTHY);
        //Настройка кнопок ---------------------------------------------------------------------

        AppCompatButton btnState = binding.buttonState;
        Drawable icon1 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_1);
        icon1.setBounds(0, 0, 75, 75);
        changeMark(btnState, icon1,false);

        AppCompatButton btnPhoto = binding.buttonPhoto;
        Drawable icon2 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_2);
        icon2.setBounds(0, 0, 75, 75);
        changeMark(btnPhoto, icon2,false);

        AppCompatButton btnTriggers = binding.buttonTriggers;
        Drawable icon3 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_3);
        icon3.setBounds(0, 0, 75, 75);
        changeMark(btnTriggers, icon3,false);

        AppCompatButton btnHealth = binding.buttonHelth;
        Drawable icon4 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_4);
        icon4.setBounds(0, 0, 75, 75);
        changeMark(btnHealth, icon4,false);

        AppCompatButton btnRemind = binding.buttonRemind;
        Drawable icon5 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_5);
        icon5.setBounds(0, 0, 75, 75);
        changeMark(btnRemind, icon5,false);

        AppCompatButton btnRate = binding.buttonRate;
        Drawable icon6 = ContextCompat.getDrawable(getActivity(), R.drawable.my_icon_healthy_6);
        icon6.setBounds(0, 0, 75, 75);
        changeMark(btnRate, icon6,false);


        viewModel.getStateChecked().observe(getViewLifecycleOwner(),(Boolean aBoolean)-> {
            Loger.log("getStateChecked "+aBoolean);
            AppCompatButton button = btnState;
            Drawable drawable=icon1;
            if (aBoolean){
                changeMark(button, drawable,true);
            } else changeMark(button, drawable,false);
        });
        viewModel.getRashPhotosChecked().observe(getViewLifecycleOwner(),(Boolean aBoolean)-> {
            Loger.log("getRashPhotosChecked "+aBoolean);
            AppCompatButton button = btnPhoto;
            Drawable drawable=icon2;
            if (aBoolean){
                changeMark(button, drawable,true);
            } else changeMark(button, drawable,false);

        });
        viewModel.getTriggersChecked().observe(getViewLifecycleOwner(),(Boolean aBoolean)-> {
            Loger.log("getTriggersChecked "+aBoolean);
            AppCompatButton button = btnTriggers;
            Drawable drawable=icon3;
            if (aBoolean){
                changeMark(button, drawable,true);
            } else changeMark(button, drawable,false);

        });
        viewModel.getStatmentChecked().observe(getViewLifecycleOwner(),(Boolean aBoolean)-> {
            Loger.log("getStatmentChecked "+aBoolean);
            AppCompatButton button = btnHealth;
            Drawable drawable=icon4;
            if (aBoolean){
                changeMark(button, drawable,true);
            } else changeMark(button, drawable,false);

        });
        viewModel.getRemindersChecked().observe(getViewLifecycleOwner(),(Boolean aBoolean)-> {
            Loger.log("getRemindersChecked "+aBoolean);
            AppCompatButton button = btnRemind;
            Drawable drawable=icon5;
            if (aBoolean){
                changeMark(button, drawable,true);
            } else changeMark(button, drawable,false);

        });
        viewModel.getRatingChecked().observe(getViewLifecycleOwner(),(Boolean aBoolean)-> {
            Loger.log("getRatingChecked "+aBoolean);
            AppCompatButton button = btnRate;
            Drawable drawable=icon6;
            if (aBoolean){
                changeMark(button, drawable,true);
            } else changeMark(button, drawable,false);

        });

        //--------------------------------------------------------------------------------------
        dateObservable.set(dateViewModel.getDate());

        //dialogfragment = new DatePickerTheme(dateViewModel.dateLive);

        dateViewModel.dateLive.observe(getViewLifecycleOwner(), (Date d)-> {
            String date=dateViewModel.getDate(d);
            bundle.putString("date",date);
            dateObservable.set(date);
        });

        Loger.log(dateViewModel.getDateUnix().toString());
        Loger.log(dateViewModel.getDate().toString());

        Long dateUnix= (dateViewModel.getDateUnix());
        if (dateUnix!=null){
            viewModel.data(dateViewModel.getDateUnix());
        }

        viewModel.isLoadedAndCalculated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    viewModel.isLoadedAndCalculated().setValue(false);
                    int sum = viewModel.getSumaryPercent();
                    binding.sum.setText(sum+"%");
                    if (sum>0){
                        binding.view.setBackground(AppCompatResources.getDrawable(requireContext(),R.drawable.hexagon_3));
                        binding.sum.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white));
                    } else {
                        binding.view.setBackground(AppCompatResources.getDrawable(requireContext(),R.drawable.hexagon_2));
                        binding.sum.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.gray_dark));
                    }
                }
            }
        });
        viewModel.getState().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s!=null){
                    binding.stateString.setText(s);
                }
            }
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
        Integer healthy_status =viewModel.getGettingDataLive().getValue().getHealth_status();
        if (gettingDataNull()|| healthy_status==null){
            Navigation.findNavController(view).navigate(R.id.stateRedactFragment);
        } else
            Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_stateFragment);

    }

    public void toPhoto(View view){
        if (gettingDataNull()|| viewModel.getGettingDataLive().getValue().getRashes().isEmpty()){
            Navigation.findNavController(view).navigate(R.id.affectedAreaRedactBodyFragment);
        } else
            Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_affectedAreasFragment);
    }

    public void toTriggers(View view){
        Integer[] triggers=viewModel.getGettingDataLive().getValue().getTriggers();
        if (gettingDataNull() || triggers.length==0){
            Navigation.findNavController(view).navigate(R.id.triggersRedactFragment);
        } else
            Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_triggersFragment);
    }

    public void toTreatment (View view){
        String topicalTherapy=viewModel.getGettingDataLive().getValue().getTopical_therapy();
        String systemicTherapy=viewModel.getGettingDataLive().getValue().getSystemic_therapy();
        String otherTreatments=viewModel.getGettingDataLive().getValue().getOther_treatments();
        if (gettingDataNull() || (topicalTherapy==null & systemicTherapy==null & otherTreatments==null)){
            Navigation.findNavController(view).navigate(R.id.treatmentRedactFragment);
        } else
            Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_treatmentFragment);
    }

    public void toReminders(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_remindersFragment3,bundle);
    }
    
    public void toRate (View view){
        Integer rate=viewModel.getGettingDataLive().getValue().getRating();
        if (gettingDataNull() || rate==null){
            Navigation.findNavController(view).navigate(R.id.ratingRedactFragment);
        } else
            Navigation.findNavController(view).navigate(R.id.action_navigation_health_diary_to_ratingFragment);
    }

    private void changeMark(AppCompatButton button, Drawable icon, Boolean on){
        Drawable checkMarker=checkMark;
        Drawable checkMarkerOff=checkMarkOff;
        if (on){
            button.setCompoundDrawables(icon,null,checkMarker,null);
        } else button.setCompoundDrawables(icon,null,checkMarkerOff,null);

    }
    private boolean gettingDataNull(){
        if (viewModel.getGettingDataLive().getValue()==null){
            return true;
        }  else return false;
    }
}