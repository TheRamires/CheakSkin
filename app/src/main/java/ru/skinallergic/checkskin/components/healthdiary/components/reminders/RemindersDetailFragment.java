package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindersViewModel;
import ru.skinallergic.checkskin.databinding.FragmentRemindersDetailBinding;

import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;

import java.util.ArrayList;
import java.util.Date;

public class RemindersDetailFragment extends BaseRemindersFragment {
    private Bundle bundle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRemindersDetailBinding binding= FragmentRemindersDetailBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(getViewModel());
        initBackGround(binding.background);

        getDateViewModel().dateLive.observe(getViewLifecycleOwner(),(Date d)-> {
            String date=getDateViewModel().getDate(d);
            binding.date.setText(date);
        });

        int position=getArguments().getInt("position");

        bundle=new Bundle();
        bundle.putInt("position",position);

        getViewModel().getRemindsLive().observe(getViewLifecycleOwner(), (ArrayList<EntityReminders> list) ->{
            EntityReminders entity=list.get(position);
            getViewModel().getEntity().set(entity);
        });

        View view=binding.getRoot();
        return view;
    }
    public void toRedact(View view){
        Navigation.findNavController(view).navigate(R.id.action_remindersDetailFragment_to_reminderRedactFragment, bundle);
    }
}