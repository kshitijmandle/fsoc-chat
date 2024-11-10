package com.fsociety.fsocietyin.models;

public class msg_model {
    String msg;
    String senderId;
    msg_model(){}
    public msg_model(String msg , String senderId){
        this.msg = msg;
        this.senderId = senderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
