package ru.skinallergic.checkskin.components.healthdiary.components;

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
import android.widget.RatingBar;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.remote.GettingData;
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RatingViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment;
import ru.skinallergic.checkskin.components.profile.NavigationFunction;
import ru.skinallergic.checkskin.databinding.FragmentRatingRedactBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import static ru.skinallergic.checkskin.components.healthdiary.components.RatingFragment.RATING_BUNDLE;

public class RatingRedactFragment extends Fragment {
    private RatingViewModel ratingViewModel;
    private DateViewModel dateViewModel;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory = App.getInstance().getAppComponent().getViewModelFactory();
        ratingViewModel = new ViewModelProvider(requireActivity(),viewModelFactory).get(RatingViewModel.class);
        dateViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel.class);
        try {
            ratingViewModel.setCurrentRating(getArguments().getInt(RATING_BUNDLE));
        }catch (Throwable ignored){};
        fragmentManager=getChildFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRatingRedactBinding binding=FragmentRatingRedactBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();

        RatingBar ratingBar=binding.ratingBar;


        Integer rating=ratingViewModel.getCurrentRating();
        Loger.log("rating "+rating);
        if (rating!=null){
            ratingBar.setRating(rating);
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingViewModel.setNewRating((int) rating);
            }
        });

        ratingViewModel.getSaved().observe(getViewLifecycleOwner(), (Boolean aBoolean)-> {
            if (aBoolean){
                popBackByStep(view);
            }
        });

        ratingViewModel.getBackSaved().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    ratingViewModel.backLoad(dateViewModel.getDateUnix());
                }
            }
        });

        ratingViewModel.getBackLoaded().observe(getViewLifecycleOwner(), new Observer<GettingData>() {
            @Override
            public void onChanged(GettingData data) {
                if (data!=null){
                    ratingViewModel.getLoaded().setValue(data);
                    popBack(view);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ratingViewModel.clearFieldsAfterRedact();
    }

    public void save(View view){
        if (ratingViewModel.isChanged()){
            ratingViewModel.save(dateViewModel.getDateUnix());
        } else popBackByStep(view);

    }

    public void backStack(View view){
        if (ratingViewModel.isChanged()){
            createDialog(view);
        } else {
            Navigation.findNavController(view).popBackStack();
        }
    }
    private void createDialog(View view) {
        ActionFunction negative = () -> {
            popBack(view);
        };
        ActionFunction positive = () -> {
            ratingViewModel.backSave(dateViewModel.getDateUnix());
        };
        NavigationFunction navigation = () -> {

        };
        DialogTwoFunctionFragment dialog = new DialogTwoFunctionFragment(
                "Сохранить изменения", negative, positive, navigation);
        dialog.show(fragmentManager, "dialog");
    }
    private void popBack(View view){
        Navigation.findNavController(view).popBackStack();

    }
    private void popBackByStep(View view){
        Navigation.findNavController(view).popBackStack(R.id.navigation_health_diary,false);
    }
}