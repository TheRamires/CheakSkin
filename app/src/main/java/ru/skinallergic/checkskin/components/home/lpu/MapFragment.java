package ru.skinallergic.checkskin.components.home.lpu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.home.viewmodels.MapViewModel;
import ru.skinallergic.checkskin.databinding.FragmentMapBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MutableLiveData<SupportMapFragment> initMapTrue=new MutableLiveData<>();
    private BottomSheetBehavior bottomSheetBehavior;
    private ViewPager2 viewPager;

    @Override
    public void onResume() {
        super.onResume();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        viewPager.setUserInputEnabled(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMapBinding binding=FragmentMapBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();

        //откл. свайп у view pager
        viewPager=requireActivity().findViewById(R.id.pager_lpu);

        // получение вью нижнего экрана
        LinearLayout llBottomSheet = (LinearLayout) view.findViewById(R.id.bottom_sheet);

        // настройка поведения нижнего экрана
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        // настройка состояний нижнего экрана
       bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
       // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        MapViewModel viewModel=new ViewModelProvider(this,viewModelFactory).get(MapViewModel.class);

        initializeMap(this);
        initMapTrue.observe(getViewLifecycleOwner(), new Observer<SupportMapFragment>() {
            @Override
            public void onChanged(SupportMapFragment supportMapFragment) {
                viewModel.mapInit(mMap,40);
            }
        });
        FloatingActionButton zoomPlus=binding.zoomPlus;
        zoomPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate cameraUpdate=CameraUpdateFactory.zoomIn();
                mMap.animateCamera(cameraUpdate);
            }
        });

        FloatingActionButton zoomMinus=binding.zoomMinus;
        zoomMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate cameraUpdate=CameraUpdateFactory.zoomOut();
                mMap.animateCamera(cameraUpdate);
            }
        });

        return view;
    }
    private void initializeMap(OnMapReadyCallback onMapReadyCallback) {
        if (mMap == null) {
            SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFrag.getMapAsync(onMapReadyCallback);
            initMapTrue.postValue(mapFrag);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}