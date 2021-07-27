package ru.skinallergic.checkskin.components.tests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.tests.adapters.AdaptePreviousRes;
import ru.skinallergic.checkskin.components.tests.data.EntityTest;
import ru.skinallergic.checkskin.components.tests.data.Result;
import ru.skinallergic.checkskin.components.tests.data.TestResult;
import ru.skinallergic.checkskin.components.tests.viewModels.TestResultViewModel;
import ru.skinallergic.checkskin.components.tests.viewModels.TestsViewModel;
import ru.skinallergic.checkskin.databinding.FragmentTestCompletionBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;

public class TestCompletionFragment extends Fragment {
    private TestsViewModel testViewModel;
    private TestResultViewModel resultViewModel;
    private EntityTest entityTest;
    private int sum;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        testViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(TestsViewModel.class);
        resultViewModel=new ViewModelProvider(this,viewModelFactory).get(TestResultViewModel.class);
        sum=getArguments().getInt("sum");
        entityTest= testViewModel.getEntityTestsObservable().get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTestCompletionBinding binding=FragmentTestCompletionBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(testViewModel);
        View view=binding.getRoot();
        binding.shadow.setY(+110);
        binding.previous.setVisibility(View.GONE);
        binding.recycler.setVisibility(View.GONE);

        setPicture(binding.img);

        List<Result> resultList=entityTest.getResults();
        for (Result result : resultList){
            int min=result.getMin();
            int max=result.getMax();
            if (sum>=min && sum<=max){
                binding.setResult(result);
                /*if(entityTest.getId()==4){
                    binding.quantity.setText(String.valueOf(sum));
                }*/
                binding.quantity.setText(String.valueOf(sum));
                break;
            }
        }
        RecyclerView recyclerView=binding.recycler;
        resultViewModel.getResult(String.valueOf(entityTest.getId()));
        resultViewModel.getResultsLive().observe(getViewLifecycleOwner(),(List<TestResult> testResults)-> {
            Loger.log("result previously "+testResults);
                if (testResults==null){return;}
                resultViewModel.getResultsLive().setValue(null);
                
                sendNewResultAfterReceiving();

                List<TestResult> fiveResults=resultViewModel.giveMeFive(testResults);

                for (TestResult result: fiveResults){
                    Loger.log("result previously fiveResults"+result);
                }

            Loger.log("result previously size"+fiveResults.size());
            if (fiveResults.size() == 0){return;}
                binding.previous.setVisibility(View.VISIBLE);
                binding.recycler.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new AdaptePreviousRes(fiveResults));
                recyclerView.setHasFixedSize(true);

        });

        /*resultViewModel.sendResult(
                String.valueOf(entityTest.getId()),
                String.valueOf(sum));*/

        if (entityTest.getId()==4){
            TextView mLink=binding.link;
            mLink.setVisibility(View.VISIBLE);
        }

        return view;
    }
    private void sendNewResultAfterReceiving(){
        Loger.log("sendNewResultAfterReceiving");
        resultViewModel.sendResult(
                String.valueOf(entityTest.getId()),
                String.valueOf(sum));
    }

    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
   /* public void finish(View view){
        Bundle bundle=new Bundle();
        bundle.putInt("idPosition", (int) testViewModel.getEntityTestsObservable().get().getId());
        Navigation.findNavController(view)
                .navigate(R.id.action_testCompletionFragment_to_testHistoryFragment, bundle);
    }*/
    public void finish(View view){
        Navigation.findNavController(view)
                .navigate(R.id.navigation_tests);
    }

    private void setPicture(ImageView img){
        int image=entityTest.getImage();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image);
        img.setImageBitmap(bitmap);
    }
}