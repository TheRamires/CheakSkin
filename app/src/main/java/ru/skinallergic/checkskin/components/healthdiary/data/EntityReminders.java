package ru.skinallergic.checkskin.components.healthdiary.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.skinallergic.checkskin.components.healthdiary.components.reminders.BaseRemindersFragment;

public class EntityReminders {
    private int id;
    private String name;
    private String description;
    private Date time;
    private Integer remind;
    private Integer repeat;

    public EntityReminders(){

    }

    public EntityReminders(int id,String name, String description, Date time, int remind, int repeat ){
        this.id=id;
        this.name=name; this.description=description; this.time=time;
        this.remind=remind;
        this.repeat=repeat;
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
        return repeat;
    }
    public void setRepeat(int repeat){
        this.repeat=repeat;
    }

    public String getRemindText(){
        String value = "";
        if (remind==null)return value;
        switch (remind){
            case 0:
                value= "Не напоминать";
                break;
            case 1:
                value= "За 5 мин";
                break;
            case 2:
                value= "За 30 мин";
                break;
            case 3:
                value= "За час";
                break;
        }
        return value;
    }

    public String getRepeatText(){
        String value = "";
        if (repeat==null)return value;
        switch (repeat){
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
