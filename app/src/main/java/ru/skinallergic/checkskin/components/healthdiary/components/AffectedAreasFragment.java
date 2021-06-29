package ru.skinallergic.checkskin.components.healthdiary.components;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogOnlyOneFunc;
import ru.skinallergic.checkskin.databinding.FragmentAffectedAreasBinding;
import ru.skinallergic.checkskin.databinding.ItemAreaBinding;
import ru.skinallergic.checkskin.databinding.ItemRashBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static ru.skinallergic.checkskin.components.healthdiary.PhotoController.GALLERY_REQUEST;
import static ru.skinallergic.checkskin.components.healthdiary.PhotoController.REQUEST_TAKE_PHOTO;

public class AffectedAreasFragment extends BaseAreaFragment {
    private AffectedAreaRedactViewModel viewModel;
    private DateViewModel dateViewModel;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private int currentPhotoId;
    private PhotoController photoController;
    private CameraPermission cameraPermission;
    private FragmentManager manager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=getChildFragmentManager();
        cameraPermission=new CameraPermission(requireActivity());
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(AffectedAreaRedactViewModel.class);
        DateViewModel dateViewModel= new ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel.class);
        viewModel.data(dateViewModel.getDateUnix());
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
                        File file=fileFromBitmap(finalBitmap, currentPhotoId);
                        viewModel.putPhotoToMap(currentPhotoId, file);
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

                        FrameLayout frameLayout0=binder.containerForPhotoUp0;
                        FrameLayout frameLayout1=binder.containerForPhotoUp1;
                        FrameLayout frameLayout2=binder.containerForPhotoUp2;
                        frameLayout0.setClipToOutline(true);
                        frameLayout1.setClipToOutline(true);
                        frameLayout2.setClipToOutline(true);

                        ImageView imageView0=binder.photoRash0;
                        ImageView imageView1=binder.photoRash1;
                        ImageView imageView2=binder.photoRash2;

                        imageView0.setOnClickListener((View v) ->{clickPhoto(imageView0);setAreaAndView(area,view);});
                        imageView1.setOnClickListener((View v) ->{clickPhoto(imageView1);setAreaAndView(area,view);});
                        imageView2.setOnClickListener((View v) ->{clickPhoto(imageView2);setAreaAndView(area,view);});

                        showByPicasso(photo1,imageView0);
                        showByPicasso(photo2,imageView1);
                        showByPicasso(photo3,imageView2);

                        //List<Integer> kindList=entity.getKind();
                        String kindTitle=AreaManager.INSTANCE.getKindTitle(entity.getKind());
                        LinearLayout kindContainer=binder.kindContainer;
                        completionKindList(new KindTemp(entity.getKind(), kindTitle), kindContainer);
                    }
        });
        recyclerView.setAdapter(adapter);
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
        switch (imageView.getId()){
            case R.id.photo_rash_0:
                currentPhotoId=0;
                break;
            case R.id.photo_rash_1:
                currentPhotoId=1;
                break;
            case R.id.photo_rash_2:
                currentPhotoId=2;
                break;
        }
        photoController.madePhoto();
    }
}