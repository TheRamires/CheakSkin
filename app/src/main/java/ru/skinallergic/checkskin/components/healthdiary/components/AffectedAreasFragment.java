package ru.skinallergic.checkskin.components.healthdiary.components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.AreaManager;
import ru.skinallergic.checkskin.components.healthdiary.CameraPermission;
import ru.skinallergic.checkskin.components.healthdiary.PhotoController;
import ru.skinallergic.checkskin.components.healthdiary.adapters.RecyclerAdapterArea;
import ru.skinallergic.checkskin.components.healthdiary.data.KindTemp;
import ru.skinallergic.checkskin.components.healthdiary.remote.Rash;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaRedactViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogOnlyOneFunc;
import ru.skinallergic.checkskin.databinding.FragmentAffectedAreasBinding;
import ru.skinallergic.checkskin.databinding.ItemAreaBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static ru.skinallergic.checkskin.components.healthdiary.PhotoController.GALLERY_REQUEST;
import static ru.skinallergic.checkskin.components.healthdiary.PhotoController.REQUEST_TAKE_PHOTO;
import static ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaRedactViewModelKt.FILE_NAME_01;
import static ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaRedactViewModelKt.FILE_NAME_02;
import static ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaRedactViewModelKt.FILE_NAME_03;

public class AffectedAreasFragment extends BaseAreaFragment {
    private RecyclerView recyclerView;
    private Boolean stumpForImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=getChildFragmentManager();
        cameraPermission=new CameraPermission(requireActivity());
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(AffectedAreaRedactViewModel.class);
        dateViewModel= new ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel.class);
        viewModel.data(dateViewModel.getDateUnix());
        stumpForImageView=false;

        //**
        viewModel.initNewMap();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getNewAreaLive().setValue(null);
        viewModel.getNewViewLive().setValue(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAffectedAreasBinding binding=FragmentAffectedAreasBinding.inflate(inflater);
        binding.setFragment(this);
        View view=binding.getRoot();
        photoController=new PhotoController(cameraPermission, this);

        //viewModel.getAffectedLists();

        recyclerView=binding.recycler;
        viewModel.getLoaded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Loger.log("aBoolean "+aBoolean);
                if (aBoolean==null){return;}
                viewModel.getLoaded().setValue(null);
                if (aBoolean){
                    viewModel.getLoaded().setValue(null);
                    if (viewModel.getOldMap().isEmpty() || viewModel.getOldMap().size()==0){
                        toRedactBody(view);
                    } else createAdapter();
                } else {toRedactBody(view);}
            }
        });

        List<String> listExample=new ArrayList<>();
        listExample.add("Крупная сыпь");
        listExample.add("Мулкая сыпь");


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case REQUEST_TAKE_PHOTO:
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    break;
            }

            Bitmap finalBitmap = PhotoController.cropToSquare(bitmap);
            if (finalBitmap!=null){
                ActionFunction actionFunction=()->{
                    imageView.setImageBitmap(finalBitmap);
                    //viewModel.addBitMapToList(finalBitmap);
                    try {
                        File file=fileFromBitmap(finalBitmap, getCurrentPhotoId());
                        viewModel.putPhotoToMap(getCurrentPhotoId(), file);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                };
                DialogOnlyOneFunc dialog=new DialogOnlyOneFunc(
                        "Фото необходимо обрезать до квадрта",
                        "Принять","Отмена",
                        actionFunction);
                dialog.show(manager,"photo");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                boolean cameraIsTrue=cameraPermission.permissionsResult(this,requestCode,grantResults);
                if (cameraIsTrue){
                    photoController.getCameraPhoto();
                }
        }
    }

    private void createAdapter(){
        RecyclerView.Adapter adapter = new RecyclerAdapterArea(viewModel.getOldMap(),
                new RecyclerAdapterArea.RecyclerCallback() {
                    @Override
                    public void bind(ItemAreaBinding binder, Rash entity) {
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
                        clickListenerForImageView(imageView0, area,view);
                        clickListenerForImageView(imageView1, area,view);
                        clickListenerForImageView(imageView2, area,view);

                        String FilePath01=showByPicasso(photo1, FILE_NAME_01,imageView0);
                        String FilePath02=showByPicasso(photo2, FILE_NAME_02,imageView1);
                        String FilePath03=showByPicasso(photo3, FILE_NAME_03,imageView2);
                        viewModel.putSavedPhotoToMap(area, view,FilePath01,  FilePath02, FilePath03);

                        List<Integer> kindList=entity.getKinds();
                        List<KindTemp> kindTemps= new ArrayList<>();
                        for (Integer index : kindList){
                            String kindTitle=AreaManager.INSTANCE.getKindTitle(index);
                            kindTemps.add(new KindTemp(index,kindTitle));
                        }

                        LinearLayout kindContainer=binder.kindContainer;
                        completionKindList(kindTemps, kindContainer);
                    }
        });
        recyclerView.setAdapter(adapter);
    }
    private void clickListenerForImageView(ImageView imageView, int area, int view){
        if (stumpForImageView){return;}
        imageView.setOnClickListener((View v) ->{clickPhoto(imageView);setAreaAndView(area,view);});
    }

    private void completionKindList(List<KindTemp> kindTemps, LinearLayout container){
        for (KindTemp kind: kindTemps){
            completionKindList(kind, container);
        }
    }

    private void completionKindList (KindTemp kind, LinearLayout container){
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View item = inflater.inflate(R.layout.item_rash, container,false);
        TextView text=item.findViewById(R.id.text);
        text.setText(kind.getTitle());
        ImageButton button=item.findViewById(R.id.btn_close);
        button.setOnClickListener((View v)-> {  });
        container.addView(item);
    }

    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }
    public void toRedact(View view){
        Navigation.findNavController(view).navigate(R.id.action_affectedAreasFragment_to_affectedAreaRedactFragment);
    }
    public void toRedactBody(View view){
        Navigation.findNavController(view).navigate(R.id.affectedAreaRedactBodyFragment);
    }
    private void setAreaAndView(int area, int view) {
        viewModel.getNewAreaLive().setValue(area);
        viewModel.getNewViewLive().setValue(view);
    }

    public void clickPhoto(View view_){
        imageView= view_.findViewById(view_.getId());
        toPhoto(imageView);
    }
/*
    //*******************************************save temp photo
    //save image
    public String imageDownload(Context ctx, String name, String pathHttp){
        String filePath=ctx.getExternalFilesDir("photo").getAbsolutePath()+"/" + name;
        Picasso.with(ctx)
                .load(pathHttp)
                .into(getTarget(filePath));
        return filePath;
    }

    //target to save
    private static Target getTarget(final String filePath){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        File file = new File(filePath);
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            System.out.println("IOException"+ e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }*/
}