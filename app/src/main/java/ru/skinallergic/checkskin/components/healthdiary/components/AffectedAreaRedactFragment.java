package ru.skinallergic.checkskin.components.healthdiary.components;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.CameraPermission;
import ru.skinallergic.checkskin.components.healthdiary.PhotoController;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogOnlyOneFunc;
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionAndMessages;
import ru.skinallergic.checkskin.databinding.FragmentAffectedAreaRedactBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;

import static android.app.Activity.RESULT_OK;
import static ru.skinallergic.checkskin.components.healthdiary.PhotoController.GALLERY_REQUEST;
import static ru.skinallergic.checkskin.components.healthdiary.PhotoController.REQUEST_TAKE_PHOTO;

public class AffectedAreaRedactFragment extends Fragment {
    private FragmentAffectedAreaRedactBinding binding;
    private AffectedAreaViewModel viewModel;
    private View view;
    private ImageView imageView;
    private CameraPermission cameraPermission;
    private PhotoController photoController;
    private FragmentManager manager;
    private int currentPhotoId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraPermission=new CameraPermission(requireActivity());
        manager=getChildFragmentManager();
        photoController=new PhotoController(cameraPermission, this);
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(AffectedAreaViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentPhotoId=0;
        binding=FragmentAffectedAreaRedactBinding.inflate(inflater);
        view=binding.getRoot();
        binding.setFragment(this);
        binding.containerForPhotoUp1.setClipToOutline(true);
        binding.containerForPhotoUp2.setClipToOutline(true);
        binding.containerForPhotoUp3.setClipToOutline(true);
        binding.containerForPhotoDown1.setClipToOutline(true);
        binding.containerForPhotoDown2.setClipToOutline(true);
        binding.containerForPhotoDown3.setClipToOutline(true);
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

            Bitmap finalBitmap = photoController.cropToSquare(bitmap);
            if (finalBitmap!=null){
                ActionFunction actionFunction=()->{
                    imageView.setImageBitmap(finalBitmap);
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

    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }

    public void toRedactBody(View view){
        Navigation.findNavController(view).navigate(R.id.action_affectedAreaRedactFragment_to_affectedAreaRedactBodyFragment);
    }

    public void save(View view){
        Navigation.findNavController(view).popBackStack();
    }

    public void clickPhoto(View view_){
        imageView= view.findViewById(view_.getId());
        switch (view_.getId()){
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
    public void sendPhotoToServer(){

    }
}