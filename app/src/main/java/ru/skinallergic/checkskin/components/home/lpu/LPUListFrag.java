package ru.skinallergic.checkskin.components.home.lpu;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.Adapters.MyRecyclerAdapter;
import ru.skinallergic.checkskin.components.home.adapters.RecyclerCallback;
import ru.skinallergic.checkskin.components.home.data.LPU;
import ru.skinallergic.checkskin.components.home.viewmodels.HomeViewModel;
import ru.skinallergic.checkskin.components.home.viewmodels.LpuViewModel;
import ru.skinallergic.checkskin.databinding.FragmentLPUListBinding;
import ru.skinallergic.checkskin.databinding.ItemLpuBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.type.Failure;
import ru.skinallergic.checkskin.type.HandleOnce;

import java.util.List;

public class LPUListFrag extends Fragment implements MyRecyclerAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ViewPager2 viewPager;

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setUserInputEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        FragmentLPUListBinding binding=FragmentLPUListBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        HomeViewModel viewModel=new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        //вкл. свайп у view pager
        viewPager=requireActivity().findViewById(R.id.pager_lpu);

        //Настройка кнопки
        AppCompatButton btnLpu=binding.btnLpu;
        Drawable icon3= ContextCompat.getDrawable(getActivity(),R.drawable.my_icon_home_3);
        icon3.setBounds(0,0,75,75);
        btnLpu.setCompoundDrawables(icon3,null,null,null);

        recyclerView=binding.recycler;

        subscribeLPU(viewModel.lpuLive);

        //-------------------NEW--------------------------------------------------------

        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        LpuViewModel lpuViewModel=new ViewModelProvider(this,viewModelFactory).get(LpuViewModel.class);
        lpuViewModel.getLpuList();
        lpuViewModel.getLpuData().observe(getViewLifecycleOwner(), new Observer<List<LPU>>() {
            @Override
            public void onChanged(List<LPU> lpus) {

            }
        });
        lpuViewModel.getFailureData().observe(getViewLifecycleOwner(), new Observer<HandleOnce<Failure>>() {
            @Override
            public void onChanged(HandleOnce<Failure> failureHandleOnce) {
                Failure failure=failureHandleOnce.getContentIfNotHandled();
                if (failure==null){
                } else {
                    Toast.makeText(requireContext(),failure.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        //------------------------------------------------------------------------------

        return view;
    }
    public void toLpuDocument (View view){
        Navigation.findNavController(view).navigate(R.id.LPUDocumentsFrag);
    }
    private void subscribeLPU(LiveData<List<LPU>> liveData){
        liveData.observe(getViewLifecycleOwner(),list->{

            MyRecyclerAdapter<LPU, ItemLpuBinding> adapter=new MyRecyclerAdapter<>(
                    list, R.layout.item_lpu, new RecyclerCallback<ItemLpuBinding, LPU>() {

                @Override
                public void bind(ItemLpuBinding binder, LPU entity) {
                    binder.setEntity(entity);
                }

            });
            adapter.setPositionListener(this);
            recyclerView.setAdapter(adapter);
        });
    }

    @Override
    public void onItemClick(View view, int id, int toLayout) {
        Bundle bundle=new Bundle();
        bundle.putInt("idPosition",id);
        Navigation.findNavController(view).navigate(toLayout, bundle);
    }
}