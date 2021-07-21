package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.adapters.SwipeRecyclerAdapterReminder;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderEntity;
import ru.skinallergic.checkskin.components.healthdiary.data.ReminderWriter;
import ru.skinallergic.checkskin.databinding.FragmentRemindersBinding;

import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;
import ru.skinallergic.checkskin.databinding.ItemAreaSwipeBinding;
import ru.skinallergic.checkskin.databinding.SwipeLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class RemindersFragment extends BaseRemindersFragment implements SwipeRecyclerAdapterReminder.OnSwipeItemClickListener, SwipeRecyclerAdapterReminder.DeleteItemClickListener,
        SwipeRecyclerAdapterReminder.OffItemClickListener {

    public static String BUNDLE_ID_OF_REMIND="idOfRemind";
    private TextView tvEmptyTextView;
    private RecyclerView mRecyclerView;
    private Bundle bundle;
    private boolean isLoaded=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRemindersBinding binding=FragmentRemindersBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(getViewModel());
        View view=binding.getRoot();

        initBackGround(binding.background);

//for example----------------------удалить это--------------------------------------------
        if (!isLoaded) {
            getViewModel().loadData();
            isLoaded=true;
        }
// --------------------------------------------------------------------------------------

        bundle=new Bundle();
        String time=getArguments().getString("date");
        if (time!=null){
            getViewModel().getDateObservable().set(time);
            bundle.putString("date",time);
        }

        tvEmptyTextView = binding.emptyView;
        mRecyclerView = binding.myRecyclerView;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getViewModelCommon().getRemind(getDateViewModel().getDateUnix()).observe(getViewLifecycleOwner(), new Observer<List<ReminderEntity>>() {
            @Override
            public void onChanged(List<ReminderEntity> mDataSet) {
                if (mDataSet.isEmpty()){

                    mRecyclerView.setVisibility(View.GONE);
                    tvEmptyTextView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    tvEmptyTextView.setVisibility(View.GONE);

                }
                SwipeRecyclerAdapterReminder adapter = new SwipeRecyclerAdapterReminder(getViewModel(),getParentFragmentManager(),mDataSet,
                        (SwipeLayoutBinding binder, ReminderEntity entity)->{
                            binder.setEntity(entity);
                            binder.clickable.setOnClickListener((View view)->{
                                Bundle bundle=new Bundle();
                                bundle.putInt(BUNDLE_ID_OF_REMIND,entity.getId());
                                Loger.log("positionId for bundle "+entity.getId());
                                navFunction(view,R.id.action_remindersFragment3_to_remindersDetailFragment,bundle);

                            });
                        });

                adapter.setOnItemClickListener(RemindersFragment.this);
                adapter.setDeleteItemClickListener(RemindersFragment.this);
                adapter.setOffItemClickListener(RemindersFragment.this);
                mRecyclerView.setAdapter(adapter);
            }
        });
/*
        getViewModel().getRemindsLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<EntityReminders>>() {
            @Override
            public void onChanged(ArrayList<EntityReminders> mDataSet) {
                if(mDataSet.isEmpty()){
                    mRecyclerView.setVisibility(View.GONE);
                    tvEmptyTextView.setVisibility(View.VISIBLE);
                }else{
                    mRecyclerView.setVisibility(View.VISIBLE);
                    tvEmptyTextView.setVisibility(View.GONE);
                }
                SwipeRecyclerAdapterReminder adapter = new SwipeRecyclerAdapterReminder(getViewModel(),getParentFragmentManager(),mDataSet,
                        (SwipeLayoutBinding binder, EntityReminders entity)->{
                            binder.setEntity(entity);
                            binder.clickable.setOnClickListener((View view)->{
                                Bundle bundle=new Bundle();
                                 bundle.putInt(BUNDLE_ID_OF_REMIND,entity.getId());
                                Loger.log("positionId for bundle "+entity.getId());
                                 navFunction(view,R.id.action_remindersFragment3_to_remindersDetailFragment,bundle);

                            });
                        });

                adapter.setOnItemClickListener(RemindersFragment.this);
                mRecyclerView.setAdapter(adapter);
            }
        });*/
        return view;
    }

    public void add (View view) {
        navFunction(view, R.id.action_remindersFragment3_to_remindersAddFragment);
    }

    @Override
    public void onItemClick(View view,int id) {
//        Bundle bundle=new Bundle();
//        bundle.putInt(BUNDLE_ID_OF_REMIND,id);
//        navFunction(view,R.id.action_remindersFragment3_to_remindersDetailFragment,bundle);
    }
    private void navFunction(View view, int id, Bundle bundle){
        getViewModel().clearEntityObservable();
        Navigation.findNavController(view).navigate(id, bundle);
    }
    private void navFunction(View view, int id ){
        getViewModel().clearEntityObservable();
        Navigation.findNavController(view).navigate(id);
    }

    @Override
    public void onItemDelete(int id) {
        getViewModelCommon().deleteRemind(id,getDateViewModel().getDateUnix());
    }

    @Override
    public void onItemOff(int id) {
        getViewModelCommon().offRemind(id,getDateViewModel().getDateUnix());
    }
}