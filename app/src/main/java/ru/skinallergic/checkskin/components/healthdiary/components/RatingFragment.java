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
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.List;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.remote.WritingData;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RatingViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.StateViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionFragment;
import ru.skinallergic.checkskin.components.profile.NavigationFunction;
import ru.skinallergic.checkskin.databinding.FragmentRatingBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;

public class RatingFragment extends Fragment {
    public static String RATING_BUNDLE= "ratingBundle";
    private RatingViewModel ratingViewModel;
    private DateViewModel dateViewModel;
    private Integer currentRating;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyViewModelFactory viewModelFactory = App.getInstance().getAppComponent().getViewModelFactory();
        ratingViewModel = new ViewModelProvider(requireActivity(),viewModelFactory).get(RatingViewModel.class);
        dateViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DateViewModel.class);
        ratingViewModel.getData(dateViewModel.getDateUnix());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRatingBinding binding=FragmentRatingBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        binding.setViewModel(ratingViewModel);
        RatingBar ratingBar=binding.ratingBar;

        ratingViewModel.getLoaded().observe(getViewLifecycleOwner(), new Observer<WritingData>() {
            @Override
            public void onChanged(WritingData writingData) {
                Loger.log("liveData "+writingData);
                if (writingData==null){return;}
                Loger.log("liveData continue"+writingData);
                Integer rating=writingData.getRating();
                if (rating!=null){
                    currentRating=rating;
                    ratingBar.setRating(writingData.getRating());
                }
            }
        });

        return view;
    }
    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
    public void toRedact(View view){
        Bundle bundle=new Bundle();
        if (currentRating!=null){
            bundle.putInt(RATING_BUNDLE,currentRating);
        }
        Navigation.findNavController(view).navigate(R.id.action_ratingFragment_to_ratingRedactFragment,bundle);
    }
}