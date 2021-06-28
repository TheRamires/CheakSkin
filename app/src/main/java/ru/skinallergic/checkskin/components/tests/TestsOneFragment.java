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


import java.util.Iterator;
import java.util.List;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.tests.data.EntityTest;
import ru.skinallergic.checkskin.components.tests.viewModels.TestsViewModel;
import ru.skinallergic.checkskin.databinding.FragmentTestsOneBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;

public class TestsOneFragment extends Fragment{
    public EntityTest entity;
    private FragmentTestsOneBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        TestsViewModel viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(TestsViewModel.class);
        binding=FragmentTestsOneBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(viewModel);
        View view=binding.getRoot();
        binding.shadow.setY(+110);

        assert getArguments() != null;
        long idPosition= getArguments().getLong("idPosition");

        List<EntityTest> list=viewModel.getTestsLive().getValue();
        Iterator iterator=list.iterator();
        while (iterator.hasNext()){
            entity= (EntityTest) iterator.next();
            if (entity.getId()==idPosition){
                viewModel.getEntityTestsObservable().set(entity);
                setPicture(entity.getImage());
                break;
            }
        }
        return view;
    }

    private void setPicture(int imageId){
        int image=imageId;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image);
        binding.img.setImageBitmap(bitmap);
    }

    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
    public void startTest(View view){
        Navigation.findNavController(view).navigate(R.id.action_testsOneFragment_to_testBeginFragment);
    }
    public void toArchive(View view){
        Navigation.findNavController(view).navigate(R.id.action_testsOneFragment_to_testHistoryFragment);
    }
}