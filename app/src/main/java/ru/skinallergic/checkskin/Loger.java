package ru.skinallergic.checkskin;

import android.util.Log;

public class Loger {
    private static final String LOG="myLog";
    private static boolean turnOn=true;  // отключить логи по всему приложению

    public static void log(Object message){
        if (turnOn) {
            Log.d(LOG, "" + message);
        }
    }
}
