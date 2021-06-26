package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindersViewModel;
import ru.skinallergic.checkskin.databinding.FragmentReminderRedactBinding;

import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.components.healthdiary.adapters.TimePickerDialogTheme;

public class RemindersRedactFragment extends BaseRemindersFragment {
    private RemindersViewModel viewModel;
    private int position;
    private DialogFragment dialogFragment;
    private MutableLiveData<String> timeLive=new MutableLiveData<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DateViewModel dateViewModel=new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        viewModel=new ViewModelProvider(requireActivity()).get(RemindersViewModel.class);
        FragmentReminderRedactBinding binding=FragmentReminderRedactBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(viewModel);

        dateViewModel.dateLive.observe(getViewLifecycleOwner(),(Date d)-> {
            String date=dateViewModel.getDate(d);
            binding.date.setText(date);
        });


        dialogFragment = new TimePickerDialogTheme(timeLive);
        timeLive.observe(getViewLifecycleOwner(), (String date) ->{
            binding.time.setText(date);
        });

        position=viewModel.getEntity().get().getId();

        View view=binding.getRoot();

        return view;
    }
    public void delete(View view){
        Loger.log(position);
        viewModel.deletePosition(position);
        Navigation.findNavController(view).popBackStack(R.id.remindersFragment3,false);
    }

    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
    public void editTime (View view){

    }
}