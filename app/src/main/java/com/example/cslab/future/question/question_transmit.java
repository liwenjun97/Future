package com.example.cslab.future.question;

import java.io.Serializable;

/**
 * Created by CSLab on 2017/7/14.
 */

public class question_transmit implements Serializable{
    private String Name;
    private String City;
    private String birth;
    private String sex;
    private String Interest;
    private String Dream;
    private String DreamPic;

    public String getDream() {
        return Dream;
    }

    public void setDream(String dream) {
        Dream = dream;
    }

    public String getDreamPic() {
        return DreamPic;
    }

    public void setDreamPic(String dreamPic) {
        DreamPic = dreamPic;
    }

    public String getInterest() {
        return Interest;
    }

    public void setInterest(String interest) {
        Interest = interest;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
