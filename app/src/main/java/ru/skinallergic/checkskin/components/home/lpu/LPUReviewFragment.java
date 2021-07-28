package ru.skinallergic.checkskin.components.home.lpu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import java.util.Objects;

import ru.skinallergic.checkskin.components.home.data.LPU;
import ru.skinallergic.checkskin.components.home.data.LpuEntity;
import ru.skinallergic.checkskin.components.home.data.LpuOneEntity;
import ru.skinallergic.checkskin.components.home.viewmodels.HomeViewModel;
import ru.skinallergic.checkskin.components.home.viewmodels.LpuViewModel;
import ru.skinallergic.checkskin.components.home.viewmodels.ReviesViewModel;
import ru.skinallergic.checkskin.databinding.FragmentLPUReviewBinding;

import static ru.skinallergic.checkskin.components.home.lpu.LPUDetailedFragment.LPU_ID;

public class LPUReviewFragment extends Fragment {
    private ReviesViewModel reviesViewModel;
    private LpuViewModel lpuViewModel;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        HomeViewModel homeViewModel=new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        reviesViewModel=new ViewModelProvider(requireActivity()).get(ReviesViewModel.class);
        lpuViewModel=new ViewModelProvider(requireActivity()).get(LpuViewModel.class);
        FragmentLPUReviewBinding binding=FragmentLPUReviewBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setBaseViewModel(reviesViewModel);
        view=binding.getRoot();

        int idPosition=getArguments().getInt(LPU_ID);
        LpuOneEntity lpuEntity= lpuViewModel.getOneLpu().getValue();
        binding.setEntity(lpuEntity);
        binding.title.setText(lpuEntity.getName());

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                reviesViewModel.setVote(Math.round(rating));
            }
        });

        binding.addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviesViewModel.saveCondition()){
                    reviesViewModel.addReview(
                            idPosition,
                            Objects.requireNonNull(reviesViewModel.getDescription()),
                            Objects.requireNonNull(reviesViewModel.getVote())
                            );
                }
            }
        });
        reviesViewModel.getSent().observe(getViewLifecycleOwner(),(sent)->{
            if (sent){
                reviesViewModel.getSent().setValue(false);
                backstack(view);
            }
        });

        return view;
    }

    public void textChanged(CharSequence s,int start, int count, int after){
        reviesViewModel.setDescription(s.toString());

    }

    public void backstack(View view){
        Navigation.findNavController(view).popBackStack();
    }
}