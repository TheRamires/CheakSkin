package ru.skinallergic.checkskin.components.fooddiary.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class EntityAllergens {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private boolean allergy=false;
    private int foodposition;

    public EntityAllergens(boolean allergy, int foodposition){
        this.allergy=allergy;
        this.foodposition=foodposition;
    }

    public void setId (int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setAllergy(boolean allergy){
        this.allergy=allergy;
    }
    public boolean isAllergy(){
        return allergy;
    }
    public void setFoodposition(int id){
        foodposition=id;
    }
    public int getFoodposition(){
        return foodposition;
    }
}
