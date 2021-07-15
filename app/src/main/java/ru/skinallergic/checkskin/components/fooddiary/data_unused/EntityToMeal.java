package ru.skinallergic.checkskin.components.fooddiary.data_unused;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class EntityToMeal {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String date;
    private String timeOfMeal;

    public EntityToMeal(String date, String timeOfMeal){
        this.date=date;
        this.timeOfMeal=timeOfMeal;
    }

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setDate (String date){
        this.date=date;
    }
    public String getDate(){
        return date;
    }
    public void setTimeOfMeal(String timeOfMeal){
        this.timeOfMeal=timeOfMeal;
    }
    public String getTimeOfMeal (){
        return timeOfMeal;
    }
}