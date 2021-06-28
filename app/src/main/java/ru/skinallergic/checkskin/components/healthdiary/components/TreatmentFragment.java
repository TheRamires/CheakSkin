package ru.skinallergic.checkskin.components.healthdiary.components;

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

import java.util.ArrayList;
import java.util.List;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.TreatmentViewModel;
import ru.skinallergic.checkskin.databinding.FragmentTreatmentBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import static ru.skinallergic.checkskin.components.healthdiary.viewModels.TreatmentViewModelKt.THERAPIES_BUNDLE;

public class TreatmentFragment extends Fragment {
    private TreatmentViewModel treatmentViewModel;
    private DateViewModel dateViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory = App.getInstance().getAppComponent().getViewModelFactory();
        treatmentViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(TreatmentViewModel.class);
        dateViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel.class);
        treatmentViewModel.getData(dateViewModel.getDateUnix());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        treatmentViewModel.getLoaded().setValue(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTreatmentBinding binding=FragmentTreatmentBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        binding.setTherapies(treatmentViewModel.getTherapiesOld());
        binding.setViewModel(treatmentViewModel);

        treatmentViewModel.getLoaded().observe(getViewLifecycleOwner(), new Observer<WritingData>() {
            @Override
            public void onChanged(WritingData writingData) {
                Loger.log("1");

                treatmentViewModel.getSplashScreenOn().set(false);
                if (writingData==null){
                    Loger.log("1 writingData ="+null);
                    return;
                }else {
                    Loger.log("1 writingData ="+writingData);
                }

                List<String> therapies=new ArrayList<>();
                String topical_therapy=writingData.getTopical_therapy();
                String systemic_therapy=writingData.getSystemic_therapy();
                String other_treatments=writingData.getOther_treatments();
                addString(therapies, topical_therapy);
                addString(therapies, systemic_therapy);
                addString(therapies, other_treatments);

                Loger.log("1 therapies "+therapies.size());
                if (therapies==null || therapies.size()==0){return;}
                treatmentViewModel.getTherapiesOld().clear();
                treatmentViewModel.getTherapiesOld().addAll(therapies);

                binding.setTherapies(treatmentViewModel.getTherapiesOld());
                treatmentViewModel.getLoaded().setValue(null);
            }
        });

        return view;
    }
    public void backStack (View view){
        Navigation.findNavController(view).popBackStack();
    }
    public void toRedact (View view){
        Bundle bundle=new Bundle();
        ArrayList<String> list = (ArrayList<String>) treatmentViewModel.getTherapiesOld();
        Loger.log("ArrayList<String> list "+list);
        bundle.putStringArrayList(THERAPIES_BUNDLE, list);
        Navigation.findNavController(view).navigate(R.id.action_treatmentFragment_to_treatmentRedactFragment,bundle);
    }
    private void addString(List<String> list, String string){
        if (string==null){
            list.add("");
        } else list.add(string);
    }
}