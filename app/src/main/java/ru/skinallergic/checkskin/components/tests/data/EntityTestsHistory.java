package ru.skinallergic.checkskin.components.tests.data;

public class EntityTestsHistory {
    private int id;
    private String name;
    private String date;
    private String score ;
    private String description;

    public EntityTestsHistory(int id, String name, String date, String description, String score ){
        this.id=id;
        this.name=name;
        this.date=date;
        this.score =score ;
        this.description=description;
    }

    public void setId (int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setDate(String date){
        this.date=date;
    }
    public String getDate(){

        return date;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getDescription(){
        return description;
    }
    public void setScore (String score ){
        this.score =score ;
    }
    public String getScore(){
        return score;
    }
}
