package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.adapters.SimpleItemTouchHelperCallback;
import ru.skinallergic.checkskin.components.healthdiary.adapters.SwipeRecyclerAdapter;
import ru.skinallergic.checkskin.databinding.FragmentRemindersBinding;

import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;

import java.util.ArrayList;

public class RemindersFragment extends BaseRemindersFragment implements SwipeRecyclerAdapter.OnItemClickListener {

    public static String BUNDLE_ID_OF_REMIND="idOfRemind";
    private TextView tvEmptyTextView;
    private RecyclerView mRecyclerView;
    private Bundle bundle;
    private boolean isLoaded=false;

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

                SwipeRecyclerAdapter adapter=new SwipeRecyclerAdapter(mDataSet);
                adapter.setOnItemClickListener(RemindersFragment.this);

                mRecyclerView.setAdapter(adapter);

                ItemTouchHelper.Callback callback =
                        new SimpleItemTouchHelperCallback(adapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(mRecyclerView);

            }
        });
        return view;
    }

    public void add (View view) {
        navFunction(view, R.id.action_remindersFragment3_to_remindersAddFragment);
    }

    @Override
    public void onItemClick(View view,int id) {
        Bundle bundle=new Bundle();
        bundle.putInt(BUNDLE_ID_OF_REMIND,id);
        navFunction(view,R.id.action_remindersFragment3_to_remindersDetailFragment,bundle);
    }
    private void navFunction(View view, int id, Bundle bundle){
        getViewModel().clearEntityObservable();
        Navigation.findNavController(view).navigate(id, bundle);
    }
    private void navFunction(View view, int id ){
        getViewModel().clearEntityObservable();
        Navigation.findNavController(view).navigate(id);
    }
}