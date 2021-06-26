package ru.skinallergic.checkskin.components.fooddiary.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EntityFoodPosition {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String name;
    private String weight;
    private boolean allergi;

    public EntityFoodPosition(String name, String weight ){
        this.name=name;
        this.weight=weight;
    }

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setWeight(String weight){
        this.weight=weight;
    }
    public String getWeight(){
        return weight;
    }
    public void setAllergi(boolean allergi){
        this.allergi=allergi;
    }
    public boolean getAllergi(){
        return allergi;
    }
}
