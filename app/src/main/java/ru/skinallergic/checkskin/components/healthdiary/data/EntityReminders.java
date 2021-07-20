package ru.skinallergic.checkskin.components.healthdiary.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragment;

import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.REMIND_IN_AN_30_MIN;
import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.REMIND_IN_AN_5_MIN;
import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.REMIND_IN_AN_HOUR;
import static ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragmentKt.REMIND_IN_AN_NEVER;


public class EntityReminders {
    private int id;
    private String name;
    private String description;
    private Date time;
    private Integer remind;
    private Integer repeatMode;
    private int kind;

    public EntityReminders(){

    }

    public EntityReminders(int id,String name, String description, Date time, int remind, int repeat,int kind ){
        this.id=id;
        this.name=name; this.description=description; this.time=time;
        this.remind=remind;
        this.repeatMode=repeat;
        this.kind=kind;
    }
    public void setKind(Integer kind){
        this.kind=kind;
    }
    public int getKind(){
        return kind;
    }
    public void setId (int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public  void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setTime(Date time){
        this.time=time;
    }
    public Date getTime(){
        return time;
    }
    public String getTimeString(){
        SimpleDateFormat simpleDateFormat=BaseRemindersFragment.Companion.getSimpleTimeParser();
        return simpleDateFormat.format(time);
    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getDescription(){
        return description;
    }
    public void setRemind(int remind){
        this.remind=remind;
    }
    public int getRemind() {
        return remind;
    }

    public int getRepeat() {
        return repeatMode;
    }
    public void setRepeat(int repeat){
        this.repeatMode=repeat;
    }

    public String getRemindText(){
        String value = "";
        if (remind==null)return value;
        switch (remind){
            case REMIND_IN_AN_NEVER:
                value= "Не напоминать";
                break;
            case REMIND_IN_AN_5_MIN:
                value= "За 5 мин";
                break;
            case REMIND_IN_AN_30_MIN:
                value= "За 30 мин";
                break;
            case REMIND_IN_AN_HOUR:
                value= "За час";
                break;
        }
        return value;
    }

    public String getRepeatText(){
        String value = "";
        if (repeatMode==null)return value;
        switch (repeatMode){
            case 0:
                value="Никогда";
                break;
            case 1:
                value="Каждый день";
                break;
            case 2:
                value="Каждую неделю";
                break;
            case 3:
                value="Каждые 2 недели";
                break;
            case 4:
                value="Каждый месяц";
                break;
            case 5:
                value="Каждый год";
                break;
        }
        return value;
    }
}
