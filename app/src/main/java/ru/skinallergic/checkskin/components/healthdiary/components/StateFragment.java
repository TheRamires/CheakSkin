package ru.skinallergic.checkskin.components.healthdiary.components;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.StateViewModel;
import ru.skinallergic.checkskin.databinding.FragmentStateBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.entrance.EntranceActivity;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import static ru.skinallergic.checkskin.components.healthdiary.HealthDiaryFragment.TAG_HEALTHY;

public class StateFragment extends Fragment {
    public static String TRIGGERS_HEALTHY_STATUS="healthStatus";
    private StateViewModel stateViewModel;
    private DateViewModel dateViewModel;
    private final int[] buttonsId=new int[3];
    private Integer currentStatus=null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory = App.getInstance().getAppComponent().getViewModelFactory();
        stateViewModel = new ViewModelProvider(requireActivity(),viewModelFactory).get(StateViewModel.class);
        dateViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel.class);
        stateViewModel.getData(dateViewModel.getDateUnix());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentStateBinding binding=FragmentStateBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(stateViewModel);
        buttonsId[0]=(R.id.radio_1); buttonsId[1]=(R.id.radio_2); buttonsId[2]=(R.id.radio_3);
        View view=binding.getRoot();

        RadioGroup radioGroup=binding.radioGroup;
        stateViewModel.getLoaded().observe(getViewLifecycleOwner(), new Observer<WritingData>() {
            @Override
            public void onChanged(WritingData writingData) {
                //stateViewModel.getSplashScreenOn().set(false);
                if (writingData==null){
                    Loger.log("writingData is null");
                    return;
                }
                if (writingData.getHealth_status()==null){
                    Loger.log("writingData.getHealth_status() is null");
                    return;
                }
                int count=writingData.getHealth_status();
                Loger.log("count "+count);
                for (int i=0;i<buttonsId.length;i++){
                    if (i==count){
                        currentStatus=buttonsId[i];
                        Loger.log("currentStatus "+currentStatus);
                        radioGroup.check(buttonsId[i]);
                        stateViewModel.getLoaded().setValue(null);
                    }
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
        Navigation.findNavController(view).popBackStack();
    }
    public void toRedact(View view){navigateToRedact(view);}

    public void navigateToRedact(View view){
        Bundle bundle=new Bundle();
        if (currentStatus!=null){
            bundle.putString(TRIGGERS_HEALTHY_STATUS,currentStatus.toString());
            Loger.log("current status "+currentStatus);
        }
        Loger.log("current status null = "+currentStatus);
        Navigation.findNavController(view).navigate(R.id.action_stateFragment_to_stateRedactFragment,bundle);
    }

    public void toEntrance() {
        Intent intent = new Intent(requireActivity(), EntranceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}