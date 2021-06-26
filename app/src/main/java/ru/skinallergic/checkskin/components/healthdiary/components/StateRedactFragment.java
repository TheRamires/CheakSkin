package ru.skinallergic.checkskin.components.healthdiary.components;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData;
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.StateViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment;
import ru.skinallergic.checkskin.components.profile.NavigationFunction;
import ru.skinallergic.checkskin.databinding.FragmentStateRedactBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.entrance.EntranceActivity;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import static ru.skinallergic.checkskin.components.healthdiary.components.StateFragment.TRIGGERS_HEALTHY_STATUS;

public class StateRedactFragment extends Fragment {
    private StateViewModel stateViewModel;
    private DateViewModel dateViewModel;
    private Integer newStatus=null;
    private Integer oldStatus=null;
    private int[] radioButtons=new int[3];
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory = App.getInstance().getAppComponent().getViewModelFactory();
        stateViewModel = new ViewModelProvider(requireActivity(),viewModelFactory).get(StateViewModel.class);
        dateViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel.class);
        fragmentManager=getChildFragmentManager();
        try {
            oldStatus=Integer.parseInt(getArguments().getString(TRIGGERS_HEALTHY_STATUS));
            Loger.log("old status from bundle "+oldStatus+"; getArg "+getArguments().getString(TRIGGERS_HEALTHY_STATUS));
        } catch (Throwable ignored){}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentStateRedactBinding binding=FragmentStateRedactBinding.inflate(inflater);
        View view=binding.getRoot();
        binding.setFragment(this);
        radioButtons[0]=(R.id.radio_1); radioButtons[1]=(R.id.radio_2); radioButtons[2]=(R.id.radio_3);

        RadioGroup radioGroup=binding.radioGroup;
        if (oldStatus!=null){ radioGroup.check(oldStatus);}

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Loger.log("radioGroup check "+checkedId);
                for (int count=0;count<radioButtons.length;count++){
                    if (radioButtons[count]==checkedId){
                        newStatus=count;
                    }
                }
            }
        });
        stateViewModel.getSaved().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    stateViewModel.getSaved().setValue(false);
                    Navigation.findNavController(view).popBackStack(R.id.navigation_health_diary,false);
                }
            }
        });

        stateViewModel.getBackSaved().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    stateViewModel.getBackSaved().setValue(false);
                    stateViewModel.backLoad(dateViewModel.getDateUnix());
                }
            }
        });

        stateViewModel.getBackLoaded().observe(getViewLifecycleOwner(), new Observer<GettingData>() {
            @Override
            public void onChanged(GettingData data) {
                if (data!=null){
                    stateViewModel.getLoaded().setValue(data);
                    stateViewModel.getBackLoaded().setValue(null);
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });
        stateViewModel.getExpiredRefreshToken().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    stateViewModel.getExpiredRefreshToken().setValue(false);
                    toEntrance();
                }
            }
        });

        return view;
    }
    public void backStack(View view){
        boolean isChanged=stateViewModel.isChanged(oldStatus,newStatus);
        Loger.log("isChanged "+isChanged);
        if (isChanged){
            createDialog(view);
        } else Navigation.findNavController(view).popBackStack();

    }
    public void save(View view){
        if (newStatus!=null){
            stateViewModel.save(dateViewModel.getDateUnix(),newStatus);
        } else Navigation.findNavController(view).popBackStack(R.id.navigation_health_diary,false);
    }

    private void createDialog(View view){
        ActionFunction negative=()-> {
            Navigation.findNavController(view).popBackStack();
        };
        ActionFunction positive=()->{
            Loger.log("dateViewModel.getDateUnix() "+dateViewModel.getDateUnix());
            Loger.log("healthStatus "+newStatus);
            stateViewModel.backSave(dateViewModel.getDateUnix(),newStatus);
        };
        NavigationFunction navigation=()-> {

        };
        DialogTwoFunctionFragment dialog=new DialogTwoFunctionFragment(
                "Сохранить изменения",negative,positive,navigation);
        dialog.show(fragmentManager,"dialog");
    }
    public void toEntrance() {
        Intent intent = new Intent(requireActivity(), EntranceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}