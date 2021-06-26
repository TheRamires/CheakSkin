package ru.skinallergic.checkskin.components.fooddiary.data;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface DaoFood {
    //Allergens--------------------------------------------------------
    @Insert
    long saveAllergens(EntityAllergens allergens);

    //FoodPosition-----------------------------------------------------
    @Insert
    Single<Long> saveFoodPosition(EntityFoodPosition foodPosition);


    //ToMeal-----------------------------------------------------------
    @Insert
    Single<Long> saveToMeal(EntityToMeal toMeal);


    //Compliance_Table--------------------------------------------------
    @Insert
    Long saveCompliance_Table(Compliance_Table compliance_tables);

    @Query("SELECT EntityFoodPosition.id," +
                "EntityFoodPosition.name, " +
                "EntityFoodPosition.weight, " +
                "EntityFoodPosition.allergi " +
            "FROM Compliance_Table " +
            "INNER JOIN EntityFoodPosition ON Compliance_Table.idFood=EntityFoodPosition.id " +
            "INNER JOIN EntityToMeal ON Compliance_Table.idToMeal=EntityToMeal.id " +
            "WHERE EntityToMeal.date=:date AND EntityToMeal.timeOfMeal=:timeOfMeal")
    Maybe<List<EntityFoodPosition>> loadFoodPositions (String date, String timeOfMeal);
}
