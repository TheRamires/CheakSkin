package ru.skinallergic.checkskin.components.tests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.tests.data.EntityTest;
import ru.skinallergic.checkskin.components.tests.data.EntityTestsHistory;
import ru.skinallergic.checkskin.components.tests.data.Result;
import ru.skinallergic.checkskin.components.tests.viewModels.TestsViewModel;
import ru.skinallergic.checkskin.databinding.FragmentTestHistoryOneBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class TestHistoryOneFragment extends Fragment {
    private TestsViewModel testViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        testViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(TestsViewModel.class);
        FragmentTestHistoryOneBinding binding=FragmentTestHistoryOneBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(testViewModel);
        View view=binding.getRoot();
        binding.shadow.setY(+110);

        EntityTest entityTest= testViewModel.getEntityTestsObservable().get();

        assert entityTest != null;
        setPicture(binding.img, entityTest.getImage());

        String string = getArguments().getString("sumResult");
        int sumResult=Integer.parseInt(string);
        /*List<EntityTestsHistory> list=new ArrayList<>(
                viewModel.getHistoryLive().getValue());
        for(EntityTestsHistory entity: list){
            if (entity.getId()==idPosition){
                binding.title.setText(entity.getName());
                binding.description.setText(entity.getDescription());
                break;
            }
        }*/

        List<Result> resultList=entityTest.getResults();
        for (Result result : resultList){
            int min=result.getMin();
            int max=result.getMax();
            if (sumResult>=min && sumResult<=max){
                binding.setResult(result);
                if(entityTest.getId()==4){
                    binding.quantity.setText(String.valueOf(sumResult));
                }
                break;
            }
        }

        return view;
    }
    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }

    private void setPicture(ImageView img, int imageId){
        int image=imageId;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image);
        img.setImageBitmap(bitmap);
    }

}