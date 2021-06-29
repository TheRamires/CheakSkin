package ru.skinallergic.checkskin.components.healthdiary;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import ru.skinallergic.checkskin.Loger;

public class MultipartManager {

    @Inject
    public MultipartManager(){}

    @NonNull
    public RequestBody createPartFromString(int value) {
        return  RequestBody.create(String.valueOf(value),MediaType.parse("text/plain"));
    }
    @NonNull
    public RequestBody createPartFromString(List<Integer> value) {
        StringBuilder sb = new StringBuilder();
        for (Integer integer : value){
            sb.append(integer);
            sb.append(", ");
        }
        String newValue=sb.toString();
        return  RequestBody.create(String.valueOf(newValue.substring(0,newValue.length()-2)),MediaType.parse("text/plain"));
    }

    @NonNull
    public MultipartBody.Part prepareFilePart(String fileName,File file) {
        //File file = new File(uri.getPath());
        RequestBody fbody = RequestBody.create(file,MediaType.parse("image/png"));

        MultipartBody.Part part = MultipartBody.Part.createFormData(fileName, file.getName(), fbody);
        return part;
    }
}
