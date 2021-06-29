package ru.skinallergic.checkskin.components.healthdiary.components;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import ru.skinallergic.checkskin.App;
import ru.skinallergic.checkskin.Loger;
import ru.skinallergic.checkskin.R;
import ru.skinallergic.checkskin.components.healthdiary.AreaManager;
import ru.skinallergic.checkskin.components.healthdiary.CameraPermission;
import ru.skinallergic.checkskin.components.healthdiary.PhotoController;
import ru.skinallergic.checkskin.components.healthdiary.viewModels.AffectedAreaRedactViewModel;
import ru.skinallergic.checkskin.components.profile.ActionFunction;
import ru.skinallergic.checkskin.components.profile.DialogOnlyOneFunc;
import ru.skinallergic.checkskin.databinding.FragmentAffectedAreaRedactBinding;
import ru.skinallergic.checkskin.databinding.FragmentAffectedAreaRedactBodyBinding;
import ru.skinallergic.checkskin.di.MyViewModelFactory;
import ru.skinallergic.checkskin.view_models.AccountViewModel;
import ru.skinallergic.checkskin.view_models.AccountViewModelImpl;
import ru.skinallergic.checkskin.view_models.DateViewModel;

import static android.app.Activity.RESULT_OK;
import static ru.skinallergic.checkskin.components.healthdiary.PhotoController.GALLERY_REQUEST;
import static ru.skinallergic.checkskin.components.healthdiary.PhotoController.REQUEST_TAKE_PHOTO;

public class AffectedAreaRedactBodyFragment extends Fragment implements Body.ClickListener, CompoundButton.OnCheckedChangeListener {
    private AffectedAreaRedactViewModel viewModel;
    private DateViewModel dateViewModel;
    private int gender;
    private View view;
    private ImageView imageView;
    private CameraPermission cameraPermission;
    private PhotoController photoController;
    private FragmentManager manager;
    private ImageView [] photoImageViewArray=new ImageView[3];
    private ToggleButton[] kindButtonsArray = new ToggleButton[6];

    private int currentPhotoId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraPermission=new CameraPermission(requireActivity());
        manager=getChildFragmentManager();
        MyViewModelFactory viewModelFactory= App.getInstance().getAppComponent().getViewModelFactory();
        viewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(AffectedAreaRedactViewModel.class);
        dateViewModel=new ViewModelProvider(requireActivity(),viewModelFactory).get(DateViewModel.class);
        AccountViewModel accountViewModel=new ViewModelProvider(requireActivity(), viewModelFactory).get(AccountViewModelImpl.class);
        gender=accountViewModel.getCurrentUser().getGender();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAffectedAreaRedactBodyBinding binding=FragmentAffectedAreaRedactBodyBinding.inflate(inflater);
        binding.setFragment(this);
        view=binding.getRoot();
        photoController=new PhotoController(cameraPermission, this);

        currentPhotoId=0;

        initImagesView(binding);
        initToggles(binding);

        cameraPermission=new CameraPermission(requireActivity());
        manager = requireActivity().getSupportFragmentManager();

        //fragment body
        FrameLayout conteiner=binding.conteiner;
        createBody(gender, conteiner);

        viewModel.getNewAreaLive().observe(getViewLifecycleOwner(), new Observer<Integer>() {
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

        viewModel.getNewViewLive().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                completionAreaText(viewModel.getNewArea(), binding);
                completionViewText(integer, binding);
                if (integer==null){return;}
                clearKindsAndImageViews();
                showAreaEntity();
            }
        });

        viewModel.getNewKindsLive().observe(getViewLifecycleOwner(), new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                if (integers==null){return;}
                Loger.log("New Kinds " +integers);
                boolean ok=viewModel.checkArea();
                if (ok){
                    viewModel.putKindsToMap(integers);
                    Loger.log("onCheckedChanged. map "+viewModel.getNewMap());
                }
            }
        });

        viewModel.getSaved().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Navigation.findNavController(view).popBackStack(R.id.navigation_health_diary, false);
                    viewModel.getSaved().setValue(false);
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getNewAreaLive().setValue(null);
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

    public void backStack(View view){
        Navigation.findNavController(view).popBackStack();
    }

    public void save(View view){
        if (viewModel.isChanged()){
            try {
                reportRequest();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else Navigation.findNavController(view).popBackStack(R.id.affectedAreasFragment, false);
    }
    @Override
    public void arround() {
        Loger.log("arround");
    }
    private void createBody(int yourGender, View conteiner){
        Body bodyFragment=Body.newInstance(yourGender,"nothing");
        bodyFragment.setClickListener(this);

        FragmentManager fragmentManager=getParentFragmentManager();
        FragmentTransaction ft =fragmentManager.beginTransaction();
        ft.add(conteiner.getId(), bodyFragment);
        ft.commit();
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
        if (viewModel.getNewArea()==null){
            Toast.makeText(getContext(),"Выберите пораженный участок тела", Toast.LENGTH_SHORT).show();
            return;
        }
        photoController.madePhoto();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Integer kind=null;
            switch (buttonView.getId()){
                case R.id.toggle_0:
                    kind=0;
                    break;
                case R.id.toggle_1:
                    kind=1;
                    break;
                case R.id.toggle_2:
                    kind=2;
                    break;
                case R.id.toggle_3:
                    kind=3;
                    break;
                case R.id.toggle_4:
                    kind=4;
                    break;
                case R.id.toggle_5:
                    kind=5;
                    break;
            }
            if (viewModel.getNewArea()==null){
                Toast.makeText(getContext(),"Выберите пораженный участок тела", Toast.LENGTH_SHORT).show();
                clearKindsAndImageViews();
                return;
            }
            if (isChecked){
                viewModel.addNewKind(kind);
            } else {viewModel.removeKind(kind);}
    }
    private void reportRequest() throws IOException, ClassNotFoundException {/*
        int size=3;
        List<Bitmap> bitmapList=viewModel.getBitmaps();

        List<File> files=new ArrayList<>();
        for (int i=0;i<bitmapList.size();i++){
            if (i==size){break;}
            Bitmap bitmap=bitmapList.get(i);
            if (bitmap==null){
                Loger.log("reportRequest. bitmap is null");
                continue;
            }
            File file=fileFromBitmap(bitmap,i);
            files.add(file);
        }
*/
        viewModel.addReport(dateViewModel.getDateUnix());
    }
    private File fileFromBitmap(Bitmap yourBitmap, int count) throws IOException, ClassNotFoundException {
        //create a file to write bitmap data
        File f = new File(requireContext().getCacheDir(), "image"+count+".png");
        f.createNewFile();

        //Convert bitmap to byte array
        Bitmap bitmap = yourBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        return f.getAbsoluteFile();
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
            int view=viewModel.getNewView();
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
            int view=viewModel.getNewView();
            if (view==0){
                text="(спереди)";
            } else text="(сзади)";
        }
        textView.setText(text);
    }

    public void showAreaEntity(){
        List<File> files=viewModel.getPhotosFromMap();
        List<Integer> kinds=viewModel.getKindsFromMap();

        if (files!=null){
            for (int i=0;i<files.size();i++){
                File file=files.get(i);
                if (file!=null){
                    photoImageViewArray[i].setImageURI(Uri.parse(file.getAbsolutePath()));
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
        viewModel.getNewKindsLive().setValue(null);
        for (int i=0;i<kindButtonsArray.length;i++){
            kindButtonsArray[i].setChecked(false);
        }
        for (int i=0;i<photoImageViewArray.length;i++){
            photoImageViewArray[i].setImageBitmap(null);
        }
    }
}