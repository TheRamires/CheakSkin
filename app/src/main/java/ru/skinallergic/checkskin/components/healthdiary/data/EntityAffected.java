package ru.skinallergic.checkskin.components.healthdiary.data;

public class EntityAffected {
    private int id;
    private String name;
    private String img;

    public EntityAffected ( int id,String name, String img){
        this.id= id;
        this.name=name;
        this.img=img;
    }

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void  setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setImg(String image){
        this.img=image;
    }
    public String getImg(){
        return img;
    }
}
