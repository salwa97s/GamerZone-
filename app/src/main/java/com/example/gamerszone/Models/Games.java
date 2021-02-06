package com.example.gamerszone.Models;

public class Games {

    private String name ;
    private int image ;
    private boolean isSelected ;
    private String code  ;

    public Games(){}

    public Games(String name, int image , boolean isSelected, String code) {
        this.name = name;
        this.image = image;
        this.isSelected = isSelected ;
        this.code=code ;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
