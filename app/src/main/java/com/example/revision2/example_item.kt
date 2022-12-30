package com.example.revision2;

public class example_item {
    String tv;
    String uid;
    String image;
    public example_item(String tv,String uid,String img) {
        this.tv = tv;
        this.uid=uid;
        image=img;
    }

    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }

    public String getUid() {
        return uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
