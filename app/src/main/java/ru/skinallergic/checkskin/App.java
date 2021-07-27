package ru.skinallergic.checkskin;
import android.app.Application;

import androidx.room.Room;
import ru.skinallergic.checkskin.components.fooddiary.data_unused.AppDatabase;
import ru.skinallergic.checkskin.di.AppComponent;
import ru.skinallergic.checkskin.di.DaggerAppComponent;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKTokenExpiredHandler;

public class App extends Application {
    private AppComponent appComponent;
    public static App instance;
    private AppDatabase database;
    private VKTokenExpiredHandler tokenTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .builder()
                .withContext(this.getApplicationContext())
                .build();

        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();

        VK.addTokenExpiredHandler(getTokenTracker());

    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
    public AppComponent getAppComponent() {
        return appComponent;
    }
    public VKTokenExpiredHandler getTokenTracker(){
        return new VKTokenExpiredHandler() {
            @Override
            public void onTokenExpired() {

            }
        };
    }
}
