package ru.skinallergic.checkskin.components.healthdiary;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogTwoFunctionAndMessages;

public class PhotoController {
    private CameraPermission cameraPermission;
    private FragmentManager manager;
    public static final int REQUEST_TAKE_PHOTO = 1;
    public static final int GALLERY_REQUEST = 2;
    public static final String PHOTO_ID="photoId";
    private Fragment fragment;

    public PhotoController(CameraPermission cameraPermission,
                           Fragment fragment){
        this.cameraPermission=cameraPermission;
        manager=fragment.getChildFragmentManager();
        this.fragment=fragment;
    }

    public void madePhoto(){
        ActionFunction cameraAction=new ActionFunction() {
            @Override
            public void action() {
                getCameraPhoto();
            }
        };
        ActionFunction galleryAction=new ActionFunction() {
            @Override
            public void action() {
                getGalleryPhoto();
            }
        };

        DialogTwoFunctionAndMessages dialog=new DialogTwoFunctionAndMessages("",
                "Сделать фото", cameraAction,
                "Выбрать из галереи", galleryAction);
        dialog.show(manager,"photo");
    }

    public void getCameraPhoto(){
        boolean cameraIsTrue=requestPermermission();
        if (cameraIsTrue) {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try{
                fragment.startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
            }catch (ActivityNotFoundException e){
                e.printStackTrace();
            }
        }
    }
    private boolean requestPermermission(){
        return cameraPermission.requestPerm(fragment);
    }

    public void getGalleryPhoto(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        fragment.startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    public static Bitmap cropToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = Math.min(height, width);
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = Math.max(cropW, 0);
        int cropH = (height - width) / 2;
        cropH = Math.max(cropH, 0);
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
    }
}
