package com.example.revision2

class ModelChat {
    var message: String? = null
    var receiver: String? = null
    var sender: String? = null
    var timestamp: String? = null
    var image: String? = null
    var userName: String? = null
    var isSeen = false

    constructor(
        isSeen: Boolean,
        message: String?,
        receiver: String?,
        sender: String?,
        timestamp: String?
    ) {
        this.isSeen = isSeen
        this.message = message
        this.receiver = receiver
        this.sender = sender
        this.timestamp = timestamp
    }

    constructor(
        message: String?,
        sender: String?,
        timestamp: String?,
        image: String?,
        UserName: String?
    ) {
        this.message = message
        this.sender = sender
        this.timestamp = timestamp
        this.image = image
        userName = UserName
    }

    constructor() {}
}
/*package com.example.revision2;

public class ModelChat {
    String message, receiver, sender, timestamp,image,UserName;

    boolean isSeen;

    public ModelChat(boolean isSeen, String message,String receiver, String sender,String timestamp) {
        this.isSeen = isSeen;
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.timestamp = timestamp;

    }
    public ModelChat(String message, String sender, String timestamp, String image, String UserName) {
        this.message = message;
        this.sender=sender;
        this.timestamp = timestamp;
        this.image=image;
        this.UserName=UserName;
    }
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String user) {
        this.UserName = user;
    }



    public  ModelChat() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
*/