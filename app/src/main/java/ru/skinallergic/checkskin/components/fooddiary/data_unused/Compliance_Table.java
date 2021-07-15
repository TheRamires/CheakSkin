package ru.skinallergic.checkskin.components.fooddiary.data_unused;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Compliance_Table {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private Long idFood;
    private Long idToMeal;

    public Compliance_Table(Long idFood, Long idToMeal){
        this.idFood=idFood;
        this.idToMeal=idToMeal;
    }

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setIdFood(Long idFood){
        this.idFood=idFood;
    }
    public Long getIdFood(){
        return idFood;
    }
    public void setIdToMeal(Long idToMeal){
        this.idToMeal=idToMeal;
    }
    public Long getIdToMeal(){
        return idToMeal;
    }
}
