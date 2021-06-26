package ru.skinallergic.checkskin.components.healthdiary.components;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaRedactViewModel;
import ru.skinallergic.checkskin.databinding.BodyManBinding;
import ru.skinallergic.checkskin.databinding.BodyWomanBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;

import static ru.skinallergic.checkskin.utils.UtilsKt.GENDER_FEMALE;
import static ru.skinallergic.checkskin.utils.UtilsKt.GENDER_MALE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Body#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Body extends Fragment{
    private AffectedAreaRedactViewModel viewModel;

    private ViewBinding binding = null;
    private Drawable bodyFront;
    private Drawable bodyBack;
    private ImageView imageView;
    private List<ToggleButton> toggles=new ArrayList<>();

    ClickListener clickListener;
    public void setClickListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GENDER = "gender";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int gender;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static Body newInstance(int gender, String param2) {
        Body fragment = new Body();
        Bundle args = new Bundle();
        args.putInt(GENDER, gender);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Body() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(AffectedAreaRedactViewModel.class);
        if (getArguments() != null) {
            gender = getArguments().getInt(GENDER);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        init(gender,inflater);
        viewModel.getFront().setValue(true);

        View view=binding.getRoot();
        imageView=view.findViewById(R.id.body);

        ToggleButton bodyCenter=view.findViewById(R.id.toggle_body_center);
        ToggleButton head=view.findViewById(R.id.toggle_head);
        ToggleButton hand1=view.findViewById(R.id.toggle_hand_1);
        ToggleButton hand2=view.findViewById(R.id.toggle_hand_2);
        ToggleButton leg1=view.findViewById(R.id.toggle_leg_1);
        ToggleButton leg2=view.findViewById(R.id.toggle_leg_2);

        toggles.add(bodyCenter); toggles.add(head); toggles.add(hand1);
        toggles.add(hand2);toggles.add(leg1); toggles.add(leg2);

        for (ToggleButton toggleButton: toggles){
            toggleListener(toggleButton);
        }

        ImageButton btnAround=view.findViewById(R.id.arround);
        btnAround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.arround();
                viewModel.getFront().setValue(!viewModel.getFront().getValue());
            }
        });

        viewModel.getFront().observe(getViewLifecycleOwner(), (Boolean front) ->{
            DisplayMetrics displayMetrics = requireContext().getResources().getDisplayMetrics();
            float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

            //button offset---------------------------------
            if (front==null){
                return;
            } else if (front){
                imageView.setImageDrawable(bodyFront);
                if (gender==GENDER_FEMALE){
                    bodyCenter.setTranslationX(dpWidth-150);
                }
            } else {
                imageView.setImageDrawable(bodyBack);
                if (gender==GENDER_FEMALE){
                    bodyCenter.setTranslationX((dpWidth)-80);
                }
            }
            setView(front);
        });

        return view;
    }
    private void setView(Boolean bool){
        if (bool){
            viewModel.getNewViewLive().setValue(0);
        } else viewModel.getNewViewLive().setValue(1);
    }

    private void init(int gender, LayoutInflater inflater){
        if (gender==GENDER_MALE){
            binding=BodyManBinding.inflate(inflater);
            bodyFront=getResources().getDrawable(R.drawable.ic_body_man_front);
            bodyBack=getResources().getDrawable(R.drawable.ic_body_man_back);

        }else if (gender==GENDER_FEMALE){
            binding= BodyWomanBinding.inflate(inflater);
            bodyFront=getResources().getDrawable(R.drawable.ic_body_woman_front);
            bodyBack=getResources().getDrawable(R.drawable.ic_body_woman_back);
        }
    }

    public interface ClickListener{
        public void arround();
    }
    private void toggleListener(ToggleButton toggleButton){
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Loger.log(toggleButton.getId());
                    clearAllToggles(toggleButton.getId());
                    Integer area=null;
                    switch (buttonView.getId()){
                        case R.id.toggle_body_center:
                            area=3;
                            break;
                        case R.id.toggle_head:
                            area=0;
                            break;
                        case R.id.toggle_hand_1:
                            area=1;
                            break;
                        case R.id.toggle_hand_2:
                            area=2;
                            break;
                        case R.id.toggle_leg_1:
                            area=4;
                            break;
                        case R.id.toggle_leg_2:
                            area=5;
                            break;
                    }
                    viewModel.getNewAreaLive().setValue(area);
                }else if (nonChecked()){
                    viewModel.getNewAreaLive().setValue(null);
                }
            }
        });
    }

    private void clearAllToggles(int except){
        for (ToggleButton toggleButton: toggles){
            if (toggleButton.getId()==except){continue;}
            toggleButton.setChecked(false);
        }
    }
    private boolean nonChecked(){
        boolean oneChecked=false;
        for (ToggleButton toggleButton: toggles){
            if (toggleButton.isChecked()){
                oneChecked=true;
                break;
            }
        }
        return !oneChecked;
    }
}