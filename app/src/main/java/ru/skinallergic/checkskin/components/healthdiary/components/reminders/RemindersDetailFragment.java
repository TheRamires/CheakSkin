package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderEntity;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderDetailViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ReminderWriterViewModel;
import ru.skinallergic.checkskin.databinding.FragmentRemindersDetailBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ru.skinallergic.checkskin.components.fooddiary.components.DetailFoodFragmentKt.BUNDLE_ID_OF_REMIND;
import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.DATE_PATTERN;


public class RemindersDetailFragment extends BaseRemindersFragment {
    private Bundle bundle;
    int positionId;
    private Spinner typeSpinner;
    private ReminderDetailViewModel reminderDetailViewModel;
    private ReminderWriterViewModel reminderWriterViewModel;
    private Boolean isDoctor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reminderDetailViewModel=new ViewModelProvider(requireActivity(),getViewModelFactory()).get(ReminderDetailViewModel.class);
        reminderDetailViewModel.getReminderDetail().set(null);
        reminderWriterViewModel=new ViewModelProvider(requireActivity(), getViewModelFactory()).get(ReminderWriterViewModel.class);
        try {
            positionId= getArguments().getInt(BUNDLE_ID_OF_REMIND);
        }catch (Throwable ignore){ }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRemindersDetailBinding binding= FragmentRemindersDetailBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(reminderDetailViewModel);
        binding.setBaseViewModel(getViewModelCommon());

        initBackGround(binding.background);
        typeSpinner=binding.type;
        ArrayAdapter<?> adapter =
                new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,getTypeList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(getTypeList().get(position));
                if (position==0){
                    isDoctor=true;
                    binding.redactDate.setVisibility(View.VISIBLE);
                } else if (position==1){
                    isDoctor=false;
                    binding.redactDate.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Loger.log("positionRemind id = "+positionId);

        getDateViewModel().dateLive.observe(getViewLifecycleOwner(),(Date d)-> {
            String date=getDateViewModel().getDate(d);
            binding.date.setText(date);
        });

        getViewModelCommon().getRemindLive().observe(getViewLifecycleOwner(), new Observer<List<ReminderEntity>>() {
            @Override
            public void onChanged(List<ReminderEntity> list) {
                if (list != null) {
                    for (ReminderEntity entity : list){
                        if(entity.getId()==positionId){
                            reminderDetailViewModel.getReminderDetail().set(entity);
                            typeSpinner.setSelection(entity.getKind());
                            binding.dateDoctor.setText(new SimpleDateFormat(DATE_PATTERN).format(entity.getStart_at()*1000));
                        }
                    }
                }
            }
        });
        /*getViewModel().getRemindsLive().observe(getViewLifecycleOwner(), (ArrayList<EntityReminders> list) ->{
            EntityReminders entity = null;
            for (EntityReminders entity_ : list){
                if (entity_.getId()==positionId) {
                    entity = entity_;
                    break;
                }
            }
            Loger.log("positionId "+positionId);
            //Loger.log("entity name"+entity.getName()+"; id "+entity.getId());
            getViewModel().getEntity().set(entity);
        });*/

        View view=binding.getRoot();
        return view;
    }
    public void toRedact(View view){
        bundle=new Bundle();
        bundle.putInt(BUNDLE_ID_OF_REMIND,positionId);
        Navigation.findNavController(view).navigate(R.id.action_remindersDetailFragment_to_reminderRedactFragment, bundle);
    }
}