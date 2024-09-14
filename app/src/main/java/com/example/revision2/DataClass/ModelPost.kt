package com.example.revision2.DataClass

class ModelPost {
    var description: String? = null
    var title: String? = null
    var image: String? = null

    constructor(description: String?, title: String?, image: String?) {
        this.description = description
        this.title = title
        this.image = image
    }

    constructor() {}
}

/*package com.example.revision2;

public class ModelPost {
    String Description,Title,Image;

    public ModelPost(String description, String title, String image) {
        Description = description;
        Title = title;
        Image = image;
    }

    public ModelPost() {
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
*/