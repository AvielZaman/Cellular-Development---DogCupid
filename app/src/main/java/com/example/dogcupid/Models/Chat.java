
package com.example.dogcupid.Models;

import java.io.Serializable;

public class Chat implements Serializable {


    private String chatId;
    private String likedDogName;
    private String userReceiverId;
    private String userSenderId;

    public Chat() {}

    public Chat(String chatId, String likedDogName, String userReceiverId,String userSenderId) {
        this.chatId = chatId;
        this.likedDogName = likedDogName;
        this.userReceiverId = userReceiverId;
        this.userSenderId = userSenderId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getLikedDogName() {
        return likedDogName;
    }

    public void setLikedDogName(String likedDogName) {
        this.likedDogName = likedDogName;
    }

    public String getUserReceiverId() {
        return userReceiverId;
    }

    public Chat setUserReceiverId(String userReceiverId) {
        this.userReceiverId = userReceiverId;
        return this;
    }

    public String getUserSenderId() {
        return userSenderId;
    }

    public Chat setUserSenderId(String userSenderId) {
        this.userSenderId = userSenderId;
        return this;
    }
}
