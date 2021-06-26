package ru.skinallergic.checkskin.components.home.data;

import androidx.annotation.NonNull;

public class Document {
    private int id;
    private String text;

    public Document(int id, String text){
        this.id=id;
        this.text=text;
    }

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setText(String text){
        this.text=text;
    }
    public String getText(){
        return text;
    }
}
