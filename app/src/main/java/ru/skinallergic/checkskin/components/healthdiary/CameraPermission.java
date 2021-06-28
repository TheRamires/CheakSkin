package ru.skinallergic.checkskin.components.healthdiary;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class CameraPermission {
    final int MY_PERMISSIONS_REQUEST_CAMERA=1;
    private Activity activity;

    public CameraPermission(Activity activity){
        this.activity=activity;
    }

    public boolean requestPerm(){
        int permissionStatus = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
            return false;
        }
    }
    public boolean permissionsResult(int requestCode, @NonNull int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CAMERA:if (grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    return true;

                }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                        Toast.makeText(activity,"Вы не сможете пользоваться камерой!", Toast.LENGTH_SHORT).show();

                    }else{
                        //Never ask again selected, or device policy prohibits the app from having that permission.
                        //So, disable that feature, or fall back to another situation...
                    }
                    return false;
                }
            }
            default: return false;
        }
    }
    public boolean requestPerm(Fragment fragment){
        int permissionStatus = ContextCompat.checkSelfPermission(fragment.getContext(), Manifest.permission.CAMERA);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {
            fragment.requestPermissions(new String[] {Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
            return false;
        }
    }
    public boolean permissionsResult(Fragment fragment, int requestCode, @NonNull int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CAMERA:if (grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    return true;

                }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    // Should we show an explanation?
                    if (fragment.shouldShowRequestPermissionRationale( Manifest.permission.CAMERA)) {
                        Toast.makeText(activity,"Вы не сможете пользоваться камерой!", Toast.LENGTH_SHORT).show();

                    }else{
                        //Never ask again selected, or device policy prohibits the app from having that permission.
                        //So, disable that feature, or fall back to another situation...
                    }
                    return false;
                }
            }
            default: return false;
        }
    }
}
