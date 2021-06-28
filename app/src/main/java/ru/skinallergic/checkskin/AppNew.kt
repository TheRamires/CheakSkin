package ru.skinallergic.checkskin

import android.app.Activity
import android.app.Application
import androidx.room.Room
import ru.skinallergic.checkskin.components.fooddiary.data.AppDatabase
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class AppNew: Application(), HasActivityInjector {
    var instance: AppNew? = null
    private var database: AppDatabase? = null


    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        instance = this
        database = Room.databaseBuilder(this, AppDatabase::class.java, "database")
                .fallbackToDestructiveMigration()
                .build()

        /*DaggerAppComponent.builder().application(this).build().inject(this)
        autoInject()*/
    }


    fun getDatabase(): AppDatabase? {
        return database
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}