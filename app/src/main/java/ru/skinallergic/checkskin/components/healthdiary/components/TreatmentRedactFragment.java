package ru.skinallergic.checkskin.components.healthdiary.components;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData;
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.StateViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TreatmentViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment;
import ru.skinallergic.checkskin.components.profile.NavigationFunction;
import ru.skinallergic.checkskin.databinding.FragmentStateRedactBinding;
import ru.skinallergic.checkskin.databinding.FragmentTreatmentRedactBinding;
import ru.skinallergic.checkskin.databinding.FragmentTriggersRedactBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.entrance.EntranceActivity;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import static ru.skinallergic.checkskin.components.healthdiary.components.StateFragment.TRIGGERS_HEALTHY_STATUS;
import static ru.skinallergic.checkskin.components.healthdiary.viewModels.TreatmentViewModelKt.OTHER_TREATMENTS_INDEX;
import static ru.skinallergic.checkskin.components.healthdiary.viewModels.TreatmentViewModelKt.SYSTEMATIC_THERAPY_INDEX;
import static ru.skinallergic.checkskin.components.healthdiary.viewModels.TreatmentViewModelKt.THERAPIES_BUNDLE;
import static ru.skinallergic.checkskin.components.healthdiary.viewModels.TreatmentViewModelKt.TOPICAL_THERAPY_INDEX;

public class TreatmentRedactFragment extends Fragment {
    private TreatmentViewModel treatmentViewModel;
    private DateViewModel dateViewModel;
    private FragmentManager fragmentManager;
    private FragmentTreatmentRedactBinding binding;
    private static String MESSAGE="Заполните хотя бы одно поле";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory = App.getInstance().getAppComponent().getViewModelFactory();
        treatmentViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(TreatmentViewModel.class);
        dateViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel.class);
        fragmentManager = getChildFragmentManager();
        try {
            Loger.log("bindle "+getArguments().getStringArrayList(THERAPIES_BUNDLE));
            treatmentViewModel.initTherapiesOld(
                    getArguments().getStringArrayList(THERAPIES_BUNDLE)
            );
        } catch (Throwable ignored) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTreatmentRedactBinding.inflate(inflater);
        View view = binding.getRoot();
        binding.setFragment(this);
        binding.setTherapies(treatmentViewModel.getTherapiesOld());

        treatmentViewModel.getSaved().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    treatmentViewModel.getSaved().setValue(false);
                    Navigation.findNavController(view).popBackStack(R.id.navigation_health_diary, false);
                }
            }
        });

        treatmentViewModel.getBackSaved().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    treatmentViewModel.getBackSaved().setValue(false);
                    treatmentViewModel.backLoad(dateViewModel.getDateUnix());
                }
            }
        });

        treatmentViewModel.getBackLoaded().observe(getViewLifecycleOwner(), new Observer<GettingData>() {
            @Override
            public void onChanged(GettingData writingData) {
                if (writingData != null) {
                    treatmentViewModel.getLoaded().setValue(writingData);
                    treatmentViewModel.getBackLoaded().setValue(null);
                    Navigation.findNavController(view).popBackStack();
                }
            }
        });
        treatmentViewModel.getExpiredRefreshToken().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    treatmentViewModel.getExpiredRefreshToken().setValue(false);
                    toEntrance();
                }
            }
        });

        treatmentViewModel.getMarksLive().observe(getViewLifecycleOwner(), new Observer<List<Boolean>>() {
            @Override
            public void onChanged(List<Boolean> booleans) {
                if (booleans==null){return;}
                Loger.log("getMarksLive "+booleans);
                Boolean topical=booleans.get(TOPICAL_THERAPY_INDEX);
                Loger.log("getMarksLive topical "+topical);
                Boolean systematic=booleans.get(SYSTEMATIC_THERAPY_INDEX);
                Loger.log("getMarksLive systematic "+systematic);
                Boolean other=booleans.get(OTHER_TREATMENTS_INDEX);
                Loger.log("getMarksLive other "+other);
                if (topical){binding.checkMark1.setVisibility(View.VISIBLE);}else binding.checkMark1.setVisibility(View.INVISIBLE);
                if (systematic){binding.checkMark2.setVisibility(View.VISIBLE);}else binding.checkMark2.setVisibility(View.INVISIBLE);
                if (other){binding.checkMark3.setVisibility(View.VISIBLE);}else binding.checkMark3.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    public void backStack(View view) {
        if (treatmentViewModel.therapyListIsChanged()){
            createDialog(view);
        }
        else Navigation.findNavController(view).popBackStack();
    }

    public void save(View view) {
        List<String> therapiesNew=treatmentViewModel.getTherapiesNew();
        if (!treatmentViewModel.checkMarkChanged()){
            Toast.makeText(requireContext(),MESSAGE,Toast.LENGTH_SHORT).show();
            Loger.log(MESSAGE);
        }else if (treatmentViewModel.isChanged()) {
            Loger.log("save");
            treatmentViewModel.save(dateViewModel.getDateUnix(), therapiesNew);
        } else {
            Loger.log("not save");
            Navigation.findNavController(view).popBackStack(R.id.navigation_health_diary, false);
        }
    }

    private void createDialog(View view) {
        List<String> therapiesNew=treatmentViewModel.getTherapiesNew();

        ActionFunction negative = () -> {
            Navigation.findNavController(view).popBackStack();
        };
        ActionFunction positive = () -> {
            if (!treatmentViewModel.checkMarkChanged()) {
                Toast.makeText(requireContext(), MESSAGE, Toast.LENGTH_SHORT).show();
                Loger.log(MESSAGE);
            } else {
                treatmentViewModel.backSave(dateViewModel.getDateUnix(), therapiesNew);
            }

        };
        NavigationFunction navigation = () -> {

        };
        DialogTwoFunctionFragment dialog = new DialogTwoFunctionFragment(
                "Сохранить изменения", negative, positive, navigation);
        dialog.show(fragmentManager, "dialog");
    }

    public void toEntrance() {
        Intent intent = new Intent(requireActivity(), EntranceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void topicalTherapyChanged(CharSequence s,int start, int count, int after){
        changeTherapyNew(s, TOPICAL_THERAPY_INDEX);
    }
    public void systemicTherapyChanged(CharSequence s,int start, int count, int after){
        changeTherapyNew(s, SYSTEMATIC_THERAPY_INDEX);
    }
    public void otherTreatmentsChanged(CharSequence s,int start, int count, int after){
        changeTherapyNew(s, OTHER_TREATMENTS_INDEX);
    }
    private void changeTherapyNew(CharSequence s, int index){
        if (s.length()>1){
            String string=s.toString();
            treatmentViewModel.getTherapiesNew().set(index,string);
            treatmentViewModel.changeMark(index, true);
        } else {
            treatmentViewModel.changeMark(index, false);
        }
        Loger.log("getTherapiesOld "+treatmentViewModel.getTherapiesOld());
        Loger.log("getTherapiesNew "+treatmentViewModel.getTherapiesNew());
    }
}