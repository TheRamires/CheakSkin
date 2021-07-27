package ru.skinallergic.checkskin.components.home.lpu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.Adapters.MyRecyclerAdapter;
import ru.skinallergic.checkskin.components.home.adapters.RecyclerCallback;
import ru.skinallergic.checkskin.components.home.data.LPU;
import ru.skinallergic.checkskin.components.home.data.LpuEntity;
import ru.skinallergic.checkskin.components.home.data.LpuOneEntity;
import ru.skinallergic.checkskin.components.home.data.ReviewEntity;
import ru.skinallergic.checkskin.components.home.viewmodels.HomeViewModel;
import ru.skinallergic.checkskin.components.home.viewmodels.LpuViewModel;
import ru.skinallergic.checkskin.components.home.viewmodels.MapViewModel;
import ru.skinallergic.checkskin.databinding.FragmentLPUDetailedBinding;
import ru.skinallergic.checkskin.databinding.ItemReviewBinding;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import ru.skinallergic.checkskin.di.MyViewModelFactory;

public class LPUDetailedFragment extends Fragment implements OnMapReadyCallback {
    private RecyclerView recyclerView;
    private GoogleMap mMap;
    private MutableLiveData<SupportMapFragment> initMapTrue=new MutableLiveData<>();// Java
    // Java
    float convertDpToPixels(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    float convertPixelsToDp(Context context, int pixels) {
        return (int) (pixels / context.getResources().getDisplayMetrics().density);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLPUDetailedBinding binding=FragmentLPUDetailedBinding.inflate(inflater);

        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        MapViewModel mapViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(MapViewModel.class);
        LpuViewModel lpuViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(LpuViewModel.class);

        binding.setFragment(this);
        View view=binding.getRoot();
        int idPosition=getArguments().getInt("idPosition");
        Loger.log("-----------------------------idPosition "+idPosition);
        lpuViewModel.getOneLpu(idPosition).observe(getViewLifecycleOwner(),(entity)->{
            LpuOneEntity lpuEntity= entity;
            binding.setEntity(lpuEntity);
        });

      /*  homeViewModel.getReviews();

        recyclerView=binding.include.recyclerReviews;
        subscribeReview(homeViewModel.reviewsLive);
*/
        initializeMap(this);
        initMapTrue.observe(getViewLifecycleOwner(), new Observer<SupportMapFragment>() {
            @Override
            public void onChanged(SupportMapFragment supportMapFragment) {
                mapViewModel.mapInit(mMap,0);
            }
        });

        Button button=binding.include.toReview;
        button.setOnClickListener((View v)-> {
                toReview(v, idPosition);
        });

        ImageButton zoomPlus=binding.zoomPlus;
        zoomPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate cameraUpdate= CameraUpdateFactory.zoomIn();
                mMap.animateCamera(cameraUpdate);
            }
        });

        ImageButton zoomMinus=binding.zoomMinus;
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
    public void toReview(View view, int idPosition){
        Bundle bundle=new Bundle();
        bundle.putInt("idPosition",idPosition);
        Navigation.findNavController(view).navigate(R.id.LPUReviewFragment,bundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    public void backstack (View view){
        Navigation.findNavController(view).popBackStack();
    }

    private void subscribeReview(LiveData<List<ReviewEntity>> liveData){
        liveData.observe(getViewLifecycleOwner(),list->{
            MyRecyclerAdapter<ReviewEntity, ItemReviewBinding> adapter=new MyRecyclerAdapter<>(
                    list, R.layout.item_review, new RecyclerCallback<ItemReviewBinding, ReviewEntity>() {
                @Override
                public void bind(ItemReviewBinding binder, ReviewEntity entity) {
                    binder.clickableLayout.setClickable(false);
                    binder.setEntity(entity);
                    RatingBar rating=binder.ratingBar;
                    rating.setRating(entity.getPoint());
                }
            });
            recyclerView.setAdapter(adapter);
        });
    }
}