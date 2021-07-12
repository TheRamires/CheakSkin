package ru.skinallergic.checkskin.components.healthdiary.components;

import android.animation.ObjectAnimator;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.AreaManager;
import ru.skinallergic.checkskin.components.healthdiary.BackNavigation;
import ru.skinallergic.checkskin.components.healthdiary.CameraPermission;
import ru.skinallergic.checkskin.components.healthdiary.PhotoController;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaCommonViewModel;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.ImageViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogFunctionFragment;
import ru.skinallergic.checkskin.components.profile.NavigationFunction;
import ru.skinallergic.checkskin.databinding.FragmentAffectedAreaRedactBodyBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.AccountViewModel;
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl;
import ru.skinallergic.checkskin.view_models.DateViewModel;

public class AffectedAreaRedactBodyFragment extends BaseAreaFragment implements Body.ClickListener, CompoundButton.OnCheckedChangeListener, Body.AreaListener{
    private Integer gender;
    private View view;
    private ImageView [] photoImageViewArray=new ImageView[3];
    private ToggleButton[] kindButtonsArray = new ToggleButton[6];
    private List<ImageView> imageViewList = new ArrayList<>();
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=getChildFragmentManager();
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModelCommon=new ViewModelProvider(requireActivity(),viewModelFactory).get(AffectedAreaCommonViewModel.class);
        dateViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel.class);
        AccountViewModel accountViewModel=new ViewModelProvider(requireActivity(), viewModelFactory).get(AccountViewModelImpl.class);
        gender=accountViewModel.getCurrentUser().getValue().getGender();
        viewModelCommon.getNewViewLive().setValue(0);
        imageViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(ImageViewModel.class);
        toastyManager=App.getInstance().getAppComponent().getToastyManager();

        //**
        viewModelCommon.copyToNewMap();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAffectedAreaRedactBodyBinding binding=FragmentAffectedAreaRedactBodyBinding.inflate(inflater);
        binding.setFragment(this);
        view=binding.getRoot();
        cameraPermission=new CameraPermission(requireActivity());
        photoController=new PhotoController(cameraPermission, this);
        imageViewList.add(binding.photoRash0);
        imageViewList.add(binding.photoRash1);
        imageViewList.add(binding.photoRash2);
        fragmentManager=requireActivity().getSupportFragmentManager();

        setCurrentPhotoId(0);

        ScrollView mScrollView= binding.scroll;
        scrollingAnimation(mScrollView);

        initImagesView(binding);
        initToggles(binding);

        manager = requireActivity().getSupportFragmentManager();

        //fragment body
        FrameLayout conteiner=binding.conteiner;
        if (gender!=null){
            createBody(gender, conteiner);
        }

        viewModelCommon.getNewAreaLive().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                completionAreaText(integer, binding);
                clearKindsAndImageViews();
                Loger.log("getNewAreaLive "+integer);
                if (integer==null){
                    return;
                } else {
                    showAreaEntity();
                }
            }
        });

        viewModelCommon.getNewViewLive().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                completionAreaText(viewModelCommon.getNewArea(), binding);
                completionViewText(integer, binding);
                if (integer==null){return;}
                clearKindsAndImageViews();
                showAreaEntity();
            }
        });

        viewModelCommon.getNewKindsLive().observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                if (integers==null){return;}
                Loger.log("New Kinds " +integers);
                boolean ok=viewModelCommon.checkArea();
                if (ok){
                    viewModelCommon.putKindsToMap(integers);
                    Loger.log("onCheckedChanged. map "+viewModelCommon.getNewMap());
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
                    Loger.log("getSomeChanged "+viewModelCommon.getSomeChanged());
                    if (getPopBackTrue()){
                        setPopBackTrue(false);
                        Navigation.findNavController(view).popBackStack(R.id.navigation_health_diary, false);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModelCommon.getNewAreaLive().setValue(null);
        viewModelCommon.setSomeChanged(false);
    }

    public void backStack(View view){
        BackNavigation popBack=()-> {Navigation.findNavController(view).popBackStack();};
        BackNavigation popBackByStep=()-> {Navigation.findNavController(view).popBackStack(R.id.navigation_health_diary,false);};

        Loger.log("is cnahfing " +viewModelCommon.isChanged());
        if (viewModelCommon.getNewMap().isEmpty() || viewModelCommon.getNewMap()==null){
            quitSaveLogic(popBackByStep);
        } else quitSaveLogic(popBack);
    }

    @Override
    public void arround() {
        Loger.log("arround");
    }
    private void createBody(int yourGender, View conteiner){
        Body bodyFragment=Body.newInstance(yourGender,"nothing");
        bodyFragment.setClickListener(this);
        bodyFragment.setAreaListener(this::clickArea);

        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction ft =fragmentManager.beginTransaction();
        ft.add(conteiner.getId(), bodyFragment);
        ft.commit();
    }

    public void clickPhoto(View view_){
        if (hasImage((ImageView)view_)){
            for (int i=0;i<imageViewList.size();i++){
                if (imageViewList.get(i)==view_){
                    System.out.println("Do you want to delete this? "+i);
                    dialogForDelete(i,imageViewList.get(i));
                    return;
                }
            }
        }
        imageView= view.findViewById(view_.getId());
        if (viewModelCommon.getNewArea()==null){
            Toast.makeText(getContext(),"Выберите пораженный участок тела", Toast.LENGTH_SHORT).show();
            return;
        }
        toPhoto(imageView);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Integer kind=AreaManager.INSTANCE.getKindFromButtonId(buttonView.getId());

            if (viewModelCommon.getNewArea()==null){
                Toast.makeText(getContext(),"Выберите пораженный участок тела", Toast.LENGTH_SHORT).show();
                clearKindsAndImageViews();
                return;
            }
            viewModelCommon.someChanging();
            if (isChecked){
                viewModelCommon.addNewKind(kind);
            } else {viewModelCommon.removeKind(kind);}
    }
    private void initImagesView(FragmentAffectedAreaRedactBodyBinding binding){
        photoImageViewArray[0]=binding.photoRash0;
        photoImageViewArray[1]=binding.photoRash1;
        photoImageViewArray[2]=binding.photoRash2;

        binding.containerForPhotoUp1.setClipToOutline(true);
        binding.containerForPhotoUp2.setClipToOutline(true);
        binding.containerForPhotoUp3.setClipToOutline(true);
    }

    private void initToggles(FragmentAffectedAreaRedactBodyBinding binding){
        ToggleButton toggleButton0=binding.toggle0;
        ToggleButton toggleButton1=binding.toggle1;
        ToggleButton toggleButton2=binding.toggle2;
        ToggleButton toggleButton3=binding.toggle3;
        ToggleButton toggleButton4=binding.toggle4;
        ToggleButton toggleButton5=binding.toggle5;

        kindButtonsArray[0]=toggleButton0;
        kindButtonsArray[1]=toggleButton1;
        kindButtonsArray[2]=toggleButton2;
        kindButtonsArray[3]=toggleButton3;
        kindButtonsArray[4]=toggleButton4;
        kindButtonsArray[5]=toggleButton5;

        for (ToggleButton toggle :kindButtonsArray ){
            toggle.setOnCheckedChangeListener(this);
        }
    }
    private void completionAreaText(Integer id,FragmentAffectedAreaRedactBodyBinding binding){
        TextView textView=binding.area;
        if (id==null){
            textView.setText("");
        } else {
            int view=viewModelCommon.getNewView();
            String text=AreaManager.INSTANCE.getTitle(id, view);
            textView.setText(text);
        }
    }
    private void completionViewText(Integer id,FragmentAffectedAreaRedactBodyBinding binding){
        TextView textView=binding.view;
        String text="";
        if (id==null){
            text="";
        } else {
            int view=viewModelCommon.getNewView();
            if (view==0){
                text="(спереди)";
            } else text="(сзади)";
        }
        textView.setText(text);
    }

    public void showAreaEntity(){
        List<File> files=viewModelCommon.getPhotosFromMap();
        List<Integer> kinds=viewModelCommon.getKindsFromMap();
        Loger.log("files get "+files,"image");

        if (files!=null){
            for (int i=0;i<files.size();i++){
                File file=files.get(i);
                if (file!=null){
                    Loger.log("file "+file);
                    showByPicasso(file,photoImageViewArray[i]);
                }
            }
        }
        if (kinds!=null){
            for (int id: kinds){
                kindButtonsArray[id].setChecked(true);
            }
        }
    }
    public void clearKindsAndImageViews(){
        viewModelCommon.getNewKindsLive().setValue(null);
        for (int i=0;i<kindButtonsArray.length;i++){
            kindButtonsArray[i].setChecked(false);
        }
        for (int i=0;i<photoImageViewArray.length;i++){
            photoImageViewArray[i].setImageBitmap(null);
        }
    }
    private void scrollingAnimation(ScrollView mScrollView){
        int speed=250;
        if (viewModelCommon.getNewMap().isEmpty()){
            speed=550;
        }
        int finalSpeed = speed;
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.setScrollY(mScrollView.getBottom());
                ObjectAnimator anim = ObjectAnimator.ofInt(mScrollView, "scrollY", 0);
                anim.setDuration(finalSpeed);
                anim.start();
            }
        });}

    @Override
    public void clickArea() { //если фото еще нет, то заполняем первую imageView
        if (viewModelCommon.cleanPosition()){
            clickPhoto(imageViewList.get(0));
            return;
        }

        if (viewModelCommon.allPositionIsNull()){
            clickPhoto(imageViewList.get(0));
        }

        //в противном случае ищем null позицию в мэпе и по ее индексу выбираем imageView
        /*List<File> fileList = viewModelCommon.getNewMap().get(
                viewModelCommon.getNewArea()
        ).get(
                viewModelCommon.getNewView()
        ).getPhotos();
        for (int i=0; i<fileList.size();i++){
            if (fileList.get(i)!=null){
                clickPhoto(imageViewList.get(i));
            }
        }*/
    }
}