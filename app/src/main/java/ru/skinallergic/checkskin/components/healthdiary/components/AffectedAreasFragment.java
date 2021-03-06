package ru.skinallergic.checkskin.components.healthdiary.components;

import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.AreaManager;
import ru.skinallergic.checkskin.components.healthdiary.BackNavigation;
import ru.skinallergic.checkskin.components.healthdiary.CameraPermission;
import ru.skinallergic.checkskin.components.healthdiary.PhotoController;
import ru.skinallergic.checkskin.components.healthdiary.adapters.SwipeRecyclerAdapterArea;
import ru.skinallergic.checkskin.components.healthdiary.data.KindTemp;
import ru.skinallergic.checkskin.components.healthdiary.remote.Rash;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaCommonViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ImageViewModel;
import ru.skinallergic.checkskin.databinding.FragmentAffectedAreasBinding;
import ru.skinallergic.checkskin.databinding.ItemAreaSwipeBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import java.util.ArrayList;
import java.util.List;

public class AffectedAreasFragment extends BaseAreaFragment {
    private AffectedAreaViewModel viewModel;
    private RecyclerView recyclerView;
    private ScrollView scrollView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=getChildFragmentManager();
        cameraPermission=new CameraPermission(requireActivity());
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModelCommon=new ViewModelProvider(requireActivity(),viewModelFactory).get(AffectedAreaCommonViewModel.class);
        dateViewModel= new ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel.class);

        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(AffectedAreaViewModel.class);
        imageViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(ImageViewModel.class);
        toastyManager=App.getInstance().getAppComponent().getToastyManager();
        //**
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModelCommon.getNewAreaLive().setValue(null);
        viewModelCommon.getNewViewLive().setValue(null);
        viewModelCommon.setSomeChanged(false);
        viewModel.redactModeOff();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //testing -
        Loger.log("AffectedAreasFragment. onDestroy and clearPhotoDirectory");
        viewModelCommon.clearPhotoDirectory(viewModelCommon.getPhotoDirectoryInMemory());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAffectedAreasBinding binding=FragmentAffectedAreasBinding.inflate(inflater);
        binding.setFragment(this);
        binding.setViewModel(viewModelCommon);
        binding.setAffectedAreaVM(viewModel);
        View view=binding.getRoot();
        photoController=new PhotoController(cameraPermission, this);
        viewModelCommon.data(dateViewModel.getDateUnix());
        viewModelCommon.setSomeChanged(false);

        recyclerView=binding.recycler;
        viewModelCommon.getLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                viewModelCommon.setSomeChanged(false);          //testing
                Loger.log("getLoaded aBoolean "+aBoolean);
                if (aBoolean==null){
                    return;
                }
                viewModelCommon.getLoaded().setValue(null);

                if (aBoolean){
                    viewModelCommon.getLoaded().setValue(null);
                    if (viewModelCommon.getOldMap().isEmpty() || viewModelCommon.getOldMap().size()==0){
                        Loger.log("viewModelCommon.getOldMap() •1 -- "+viewModelCommon.getOldMap().isEmpty()+", "+viewModelCommon.getOldMap().size());
                        toRedactBody(view);
                    } else {
                        Loger.log("viewModelCommon.getOldMap() •2");
                        createAdapter();
                    }
                } else {

                    Loger.log("viewModelCommon.getOldMap() •3");
                    toRedactBody(view);
                }
            }
        });

        viewModelCommon.getSaved().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    viewModelCommon.getSaved().setValue(false);
                    toastyManager.toastyyyy("Данные успешно сохранены");
                    viewModelCommon.setSomeChanged(false);
                    if (getPopBackTrue()){
                        setPopBackTrue(false);
                        Navigation.findNavController(view).popBackStack(R.id.navigation_health_diary, false);
                    } else viewModelCommon.data(dateViewModel.getDateUnix(), viewModelCommon.getProgressBar());
                }
            }
        });

        List<String> listExample=new ArrayList<>();
        listExample.add("Крупная сыпь");
        listExample.add("Мелкая сыпь");

        scrollView=binding.scrollView;
        viewModel.getRedactModeLive().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (!aBoolean && viewModelCommon.getSomeChanged()){
                    viewModelCommon.data(dateViewModel.getDateUnix());

                }
            }
        });

        return view;
    }

    private void savePhotoPath(){
        List<Rash> rashList = viewModelCommon.getOldMap();
        if (rashList==null){return;}
        for (Rash rash: rashList){
            if (rash==null){continue;}
            String filePath01=savePath(rash.getPhoto_1());
            String filePath02=savePath(rash.getPhoto_2());
            String filePath03=savePath(rash.getPhoto_3());
            viewModelCommon.putSavedPhotoToOldMap(rash.getArea(), rash.getView(), filePath01,  filePath02, filePath03);
        }
        viewModelCommon.copyToNewMap();
    }

    private void createAdapter(){
        Loger.log("************ in createAdapter. oldMap "+viewModelCommon.getOldMap());

        RecyclerView.Adapter adapter = new SwipeRecyclerAdapterArea(viewModelCommon,getParentFragmentManager(),viewModelCommon.getOldMap(),
                new SwipeRecyclerAdapterArea.RecyclerCallback() {
                    @Override
                    public void bind(ItemAreaSwipeBinding itemAreaSwipeBinding, Rash entity ) {
                        ItemAreaSwipeBinding binder=itemAreaSwipeBinding;  //*** testing

                        entity.getId();

                        Integer area=entity.getArea();
                        Integer view=entity.getView();
                        String photo1=entity.getPhoto_1();
                        String photo2=entity.getPhoto_2();
                        String photo3=entity.getPhoto_3();

                        String areaTitle=AreaManager.INSTANCE.getTitle(area,view);
                        String viewTitle=AreaManager.INSTANCE.getViewTitle(view);
                        binder.title.setText(areaTitle);
                        binder.viewTitle.setText(viewTitle);

                        //containers
                        FrameLayout frameLayout0=binder.containerForPhotoUp0;
                        FrameLayout frameLayout1=binder.containerForPhotoUp1;
                        FrameLayout frameLayout2=binder.containerForPhotoUp2;
                        frameLayout0.setClipToOutline(true);
                        frameLayout1.setClipToOutline(true);
                        frameLayout2.setClipToOutline(true);

                        ImageView imageView0=binder.photoRash0;
                        ImageView imageView1=binder.photoRash1;
                        ImageView imageView2=binder.photoRash2;

                        //clickListener
                        /*imageView0.setOnClickListener((View v) ->{clickPhoto(imageView0);setAreaAndView(area,view);});
                        imageView1.setOnClickListener((View v) ->{clickPhoto(imageView1);setAreaAndView(area,view);});
                        imageView2.setOnClickListener((View v) ->{clickPhoto(imageView2);setAreaAndView(area,view);});*/
                        clickListenerForImageView(imageView0, area,view,0);
                        clickListenerForImageView(imageView1, area,view,1);
                        clickListenerForImageView(imageView2, area,view,2);

                        showByPicasso(photo1, imageView0);
                        showByPicasso(photo2, imageView1);
                        showByPicasso(photo3, imageView2);
                        //   Loger.log("path photo 1, "+FilePath01+"\n path photo 2 "+FilePath02+"\n path photo 3 "+FilePath03);

                        // viewModelCommon.putSavedPhotoToOldMap(area, view, FilePath01,  FilePath02, FilePath03);
                        // viewModelCommon.copyToNewMap();

                        //kindTemps.clear();
                         List<KindTemp> kindTemps = new ArrayList<>();
                        List<Integer> kindList=entity.getKinds();
                        Loger.log("******* inAdapter. area "+area+",; view"+view+"; kindList "+kindList);
                        for (Integer index : kindList){
                            String kindTitle=AreaManager.INSTANCE.getKindTitle(index);
                            kindTemps.add(new KindTemp(index,kindTitle));
                        }

                        LinearLayout kindContainer=binder.kindContainer;
                        kindContainer.removeAllViews();
                        completionKindList(kindTemps, kindContainer, area,view);

                    }
        });
        recyclerView.setAdapter(adapter);
    }

    private void clickListenerForImageView(ImageView imageView, int area, int view, int index){
        imageView.setOnClickListener((View v) ->{
            if (!viewModel.redactModeIsOn()){
                Drawable drawable=imageView.getDrawable();
                if (drawable!=null){
                    showPhotoDialog(drawable);
                }
            }

            clickPhoto(imageView,index);
            setAreaAndView(area,view);
        });
    }

    private void completionKindList(List<KindTemp> kindTemps, LinearLayout container, int area, int view){
        for (int i=0; i<kindTemps.size();i++){
            KindTemp kind=kindTemps.get(i);
            int count=i;
            //completionKindList(kind, container,count,area,view);

            LayoutInflater inflater = LayoutInflater.from(requireContext());
            View item = inflater.inflate(R.layout.item_rash, container,false);
            TextView text=item.findViewById(R.id.text);
            text.setText(kind.getTitle());
            ImageButton button=item.findViewById(R.id.btn_close);
            button.setOnClickListener((View v)-> {
                if (viewModel.redactModeIsOn()){
                    if (kindTemps.size()==1){
                        Toast.makeText(getContext(),"Должен быть указан как минимум один тип сыпи", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    deleteKindPosition(kindTemps,count, container,area,view);
                }
            });
            container.addView(item);
        }
    }

    private void deleteKindPosition(List<KindTemp> kindTemps,int indx, LinearLayout container, int area, int view){
        Loger.log("delete indx "+indx+" container"+container);
        container.removeAllViews();
        kindTemps.remove(indx);
        completionKindList(kindTemps, container,area,view);
        List<Integer> kindIndexes= new ArrayList();
        for (KindTemp kindTemp : kindTemps){
            kindIndexes.add(kindTemp.getIndex());
        }
        viewModelCommon.someChanging();
        viewModelCommon.putKindsToMap(kindIndexes, area, view);
    }

    public void backStack(View view){
        BackNavigation pop=() ->{ viewModel.redactModeOff();};

        Boolean redactMode=viewModel.getRedactModeLive().getValue();
        if (redactMode == null || redactMode==false){
            Navigation.findNavController(view).popBackStack();
        } else {quitSaveLogic(pop);}
    }

    public void redactMode(View view){
        /*Loger.log("redact");
        savePhotoPath();
        viewModel.redactModeOn();
        scrollingAnimation(scrollView);*/
        //testing********************************
        savePhotoPath();
        toRedactBody(view);
        //testing********************************
    }
    public void toRedactBody(View view){
        Navigation.findNavController(view).navigate(R.id.action_affectedAreasFragment_to_affectedAreaRedactBodyFragment);
    }
    private void setAreaAndView(int area, int view) {
        viewModelCommon.getNewAreaLive().setValue(area);
        viewModelCommon.getNewViewLive().setValue(view);
    }

    public void clickPhoto(ImageView imageView_, int index){
        if (viewModel.redactModeIsOn()){
            if (hasImage(imageView_)){
                System.out.println("Do you want to delete this? "+index);
                dialogForDelete(index,imageView_);
                return;
            }
            imageView= imageView_;
            toPhoto(imageView);
        }
    }

    private void scrollingAnimation(ScrollView mScrollView){
        //mScrollView.scrollTo(0,mScrollView.getBottom());
        int speed=250;
        int finalSpeed = speed;
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                //mScrollView.scrollTo(0,mScrollView.getBottom());
                ObjectAnimator anim = ObjectAnimator.ofInt(mScrollView, "scrollY", mScrollView.getBottom());
                anim.setDuration(finalSpeed);
                anim.start();
            }
        });}
}