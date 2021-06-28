package ru.skinallergic.checkskin.components.tests;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.tests.adapters.RecyclerAdapter_test_history;
import ru.skinallergic.checkskin.components.tests.data.EntityTest;
import ru.skinallergic.checkskin.components.tests.data.EntityTestsHistory;
import ru.skinallergic.checkskin.components.tests.data.TestResult;
import ru.skinallergic.checkskin.components.tests.viewModels.TestResultViewModel;
import ru.skinallergic.checkskin.components.tests.viewModels.TestsViewModel;
import ru.skinallergic.checkskin.databinding.FragmentTestHistoryBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class TestHistoryFragment extends Fragment {
    public ObservableField<Boolean> progressBarOn = new ObservableField();
    private TestsViewModel testsViewModel;
    private TestResultViewModel resultViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        testsViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(TestsViewModel.class);
        resultViewModel =new ViewModelProvider(requireActivity(),viewModelFactory).get(TestResultViewModel.class);
        EntityTest entityTest=testsViewModel.getEntityTestsObservable().get();

        resultViewModel.getResult(String.valueOf(entityTest.getId()));
        progressBarOn.set(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTestHistoryBinding binding=FragmentTestHistoryBinding.inflate(inflater);

        binding.setFragment(this);
        binding.setViewModel(testsViewModel);
        View view=binding.getRoot();

        RecyclerView recyclerView=binding.recycler;

        /*testsViewModel.getHistory();
        testsViewModel.getHistoryLive().observe(getViewLifecycleOwner(), new Observer<List<EntityTestsHistory>>() {
            @Override
            public void onChanged(List<EntityTestsHistory> entityTestsHistories) {
                RecyclerView.Adapter adapter=new RecyclerAdapter_test_history(entityTestsHistories);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(false);
            }
        });*/

        /*try {
            int idPosition = getArguments().getInt("idPosition");
            List<EntityTestsHistory> list=new ArrayList<>(
                    viewModel.getHistoryLive().getValue());
            for(EntityTestsHistory entity: list){
                if (entity.getId()==idPosition){
                    binding.title.setText(entity.getName());
                    break;
                }
            }
        } catch (Exception e) {}*/

        resultViewModel.getResultsLive().observe(getViewLifecycleOwner(),(List<TestResult> testResults) -> {
            if (testResults==null){return;}
            RecyclerView.Adapter adapter=new RecyclerAdapter_test_history(testResults);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            progressBarOn.set(false);
            if (adapter.getItemCount()==0){
                Toast.makeText(getContext(),"Пока что нет пройденных тестов", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resultViewModel.clearResults();
    }
}