package com.itheima.pojo;

import java.io.Serializable;

public class DelSetmealPojo implements Serializable {
    private Integer id;
    private String img;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
