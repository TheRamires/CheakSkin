package ru.skinallergic.checkskin.components.home.data;

public class LPUDoc {
    private int id;
    private int packageIds;
    private String description;

    public void setId(int id){
        this.id=id;
    }
    public int getId(){
        return id;
    }
    public void setPackageIds(int packageIds){this.packageIds=packageIds;}
    public int getPackageIds(){return packageIds;}
    public void setDescription(String description){
        this.description=description;
    }
    public String getDescription(){
        return description;
    }
}
