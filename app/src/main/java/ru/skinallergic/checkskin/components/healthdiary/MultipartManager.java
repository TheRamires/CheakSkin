package ru.skinallergic.checkskin.components.healthdiary;

import androidx.annotation.NonNull;

import java.io.File;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartManager {

    @Inject
    public MultipartManager(){}

    @NonNull
    public RequestBody createPartFromString(int value) {
        return  RequestBody.create(String.valueOf(value),MediaType.parse("text/plain"));
    }

    @NonNull
    public MultipartBody.Part prepareFilePart(String fileName,File file) {
        //File file = new File(uri.getPath());
        RequestBody fbody = RequestBody.create(file,MediaType.parse("image/png"));

        MultipartBody.Part part = MultipartBody.Part.createFormData(fileName, file.getName(), fbody);
        return part;
    }
}
