package ru.skinallergic.checkskin.components.tests;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.OwnerTimeCrutch;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.tests.data.EntityTest;
import ru.skinallergic.checkskin.components.tests.viewModels.TestsViewModel;
import ru.skinallergic.checkskin.databinding.ItemTests2Binding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;
import ru.skinallergic.checkskin.components.tests.adapters.RecyclerAdapter_tests;
import ru.skinallergic.checkskin.databinding.TestsFragmentBinding;

import java.util.List;

public class TestsFragment extends Fragment implements RecyclerAdapter_tests.Binder {
    private TestsFragmentBinding binding;
    private DateViewModel dateViewModel;
    MyViewModelFactory viewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        dateViewModel=new ViewModelProvider(requireActivity()).get(DateViewModel.class);
        OwnerTimeCrutch.INSTANCE.crutch(dateViewModel.dateLive);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TestsViewModel viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(TestsViewModel.class);
        binding=TestsFragmentBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();

        viewModel.getDate();

        binding.date.setText(dateViewModel.getDate());

        RecyclerView recyclerView=binding.recycler;
        viewModel.getTestsLive().observe(getViewLifecycleOwner(),(List<EntityTest> entityTests) ->{
            RecyclerAdapter_tests adapter=new RecyclerAdapter_tests(entityTests);
            adapter.notifyDataSetChanged();
            adapter.setBinder(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(false);
        });


        return view;
    }
    public void toProfile(View view){
        Navigation.findNavController(view).navigate(R.id.profileFragment);
    }

    @Override
    public void bind(ItemTests2Binding binding, EntityTest position) {
        EntityTest entity=position;
        int image=entity.getImage();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), image);
        binding.testImage.setImageBitmap(bitmap);
        binding.setEntity(entity);
        binding.cardView.setOnClickListener((View v)-> {
            Bundle bundle=new Bundle();
            bundle.putLong("idPosition", position.getId());
            Navigation.findNavController(v).navigate(R.id.testsOneFragment,bundle);
        });
    }
}