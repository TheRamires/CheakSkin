package ru.skinallergic.checkskin.components.home.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseItem {
    @SerializedName("id")
    @Expose
    private int id;

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
}
