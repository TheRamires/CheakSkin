package ru.skinallergic.checkskin.components.home.components;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.databinding.FragmentLpuBinding;

import ru.skinallergic.checkskin.components.home.viewmodels.HomeViewModel;
import ru.skinallergic.checkskin.components.home.data.LPU;

import java.util.List;

public class LPUFragment extends Fragment {
    private FragmentLpuBinding binding;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLpuBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        bundle=new Bundle();
        HomeViewModel viewModel=new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        viewModel.lpuLive.observe(getViewLifecycleOwner(), new Observer<List<LPU>>() {
            @Override
            public void onChanged(List<LPU> lpus) {
                LPU entity=lpus.get(0);
                binding.setEntity(entity);
                binding.further.setBackground(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_further_orange));
                bundle.putInt("id",entity.getId());
            }
        });

        return view;
    }
    public void toDetail(View view){

        Navigation.findNavController(view).navigate(R.id.LPUDetailedFragment,bundle);
    }
}