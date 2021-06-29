package ru.skinallergic.checkskin.components.healthdiary.components;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaRedactViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaViewModel;
import ru.skinallergic.checkskin.databinding.FragmentAffectedAreasBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import java.util.ArrayList;
import java.util.List;

public class AffectedAreasFragment extends Fragment {
    private AffectedAreaRedactViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAffectedAreasBinding binding=FragmentAffectedAreasBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();

        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(AffectedAreaRedactViewModel.class);
        DateViewModel dateViewModel= new ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel.class);
        //viewModel.getAffectedLists();

        viewModel.data(dateViewModel.getDateUnix());

        RecyclerView recyclerView1=binding.recycler1;

        List<String> listExample=new ArrayList<>();
        listExample.add("Крупная сыпь");
        listExample.add("Мулкая сыпь");


        return view;
    }
    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
    public void toRedact(View view){
        Navigation.findNavController(view).navigate(R.id.action_affectedAreasFragment_to_affectedAreaRedactFragment);
    }
}