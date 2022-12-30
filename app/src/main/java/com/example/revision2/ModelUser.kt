package com.example.revision2

class ModelUser(var name: String, var email: String, var phone: String, var uid: String) {
    var image: String? = null

}
/*package com.example.revision2;

public class ModelUser{
    String name,email,phone,image,uid;
    public ModelUser(String name, String email,String phone, String uid){
        this.email=email;
        this.name=name;
        this.phone=phone;
        this.uid=uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }
}
*/