package ru.skinallergic.checkskin.components.healthdiary.components.reminders;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.RemindersViewModel;
import ru.skinallergic.checkskin.databinding.FragmentRemindersBinding;

import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.components.healthdiary.adapters.SwipeRecyclerViewAdapter;
import ru.skinallergic.checkskin.components.healthdiary.data.EntityReminders;

import java.util.ArrayList;
import java.util.Date;

public class RemindersFragment extends BaseRemindersFragment {

    private TextView tvEmptyTextView;
    private RecyclerView mRecyclerView;
    private RemindersViewModel viewModel;
    private Bundle bundle;
    private boolean isLoaded=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DateViewModel dateViewModel=new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        viewModel=new ViewModelProvider(requireActivity()).get(RemindersViewModel.class);
        FragmentRemindersBinding binding=FragmentRemindersBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(viewModel);
        View view=binding.getRoot();

        initBackGround(binding.background);

//for example----------------------удалить это--------------------------------------------
        if (!isLoaded) {
            viewModel.loadData();
            isLoaded=true;
        }
// --------------------------------------------------------------------------------------

        bundle=new Bundle();
        String time=getArguments().getString("date");
        if (time!=null){
            viewModel.getDateObservable().set(time);
            bundle.putString("date",time);
        }

        tvEmptyTextView = binding.emptyView;
        mRecyclerView = binding.myRecyclerView;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getRemindsLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<EntityReminders>>() {
            @Override
            public void onChanged(ArrayList<EntityReminders> mDataSet) {

                if(mDataSet.isEmpty()){
                    mRecyclerView.setVisibility(View.GONE);
                    tvEmptyTextView.setVisibility(View.VISIBLE);
                }else{
                    mRecyclerView.setVisibility(View.VISIBLE);
                    tvEmptyTextView.setVisibility(View.GONE);
                }

                SwipeRecyclerViewAdapter mAdapter = new SwipeRecyclerViewAdapter(getActivity(), mDataSet);
                mAdapter.notifyDataSetChanged();

                ((SwipeRecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);

                mRecyclerView.setAdapter(mAdapter);

                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        Log.e("RecyclerView", "onScrollStateChanged");
                    }
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });

            }
        });
        return view;
    }

    public void add (View view){Navigation.findNavController(view).
            navigate(R.id.action_remindersFragment3_to_remindersAddFragment);}
}