package ru.skinallergic.checkskin.components.healthdiary.data;

public class EntityReminders {
    private int id;
    private String name;
    private String description;
    private String time;
    private String remind;
    private String repeat;

    public EntityReminders(int id,String name, String description, String time, String remind, String repeat ){
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
    public void setTime(String time){
        this.time=time;
    }
    public String getTime(){
        return time;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getDescription(){
        return description;
    }
    public void setRemind(String remind){
        this.remind=remind;
    }
    public String getRemind(){
        return remind;
    }
    public void setRepeat(String repeat){
        this.repeat=repeat;
    }
    public String getRepeat(){
        return repeat;
    }
}
