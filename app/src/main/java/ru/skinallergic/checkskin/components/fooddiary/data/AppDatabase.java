package ru.skinallergic.checkskin.components.fooddiary.data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {EntityAllergens.class, EntityFoodPosition.class,
                        EntityToMeal.class,Compliance_Table.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoFood daoFood();
}
