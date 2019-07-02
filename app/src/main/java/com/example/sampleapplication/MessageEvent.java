package com.example.sampleapplication;


import java.util.ArrayList;

public class MessageEvent {

    public static  String message ;

    public MessageEvent(String message) {
        this.message = message;
    }
    public static String getMessage(){
        return message;
    }
}