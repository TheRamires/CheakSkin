package ru.skinallergic.checkskin.components.home.data;

public class ReviewEntity extends BaseItem {
    //id from BaseItem
    private int idOfLpu;

    private float point;
    private String name;
    private String text;
    private String date;

    public ReviewEntity(int idOfLpu, float point, String name,String text, String date){
        this.point=point;
        this.name=name;
        this.text=text;
        this.date=date;
        this.idOfLpu=idOfLpu;
    }
    public void setIdOfLpu(int idOfLpu){
        this.idOfLpu=idOfLpu;
    }
    public int getIdOfLpu(){
        return idOfLpu;
    }

    public void setPoint(float point){
        this.point=point;
    }
    public float getPoint(){
        return point;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return  name;
    }
    public void setText(String text){
        this.text=text;
    }
    public String getText(){
        return text;
    }
    public void setDate(String date){
        this.date=date;
    }
    public String getDate(){
        return  date;
    }
}
