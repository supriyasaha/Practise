package com.example.coupondunia.babajobtest;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class BabaJob extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(UserInfoObject.class);
        Parse.initialize(this,"j2ArDjONUHSkiDmhRd9M1YdCgudcQKeoWXEGFIyo","kw3iHhMnUH2BAiuqOBvjWLLbhKaWivA1bKf17SBC");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
