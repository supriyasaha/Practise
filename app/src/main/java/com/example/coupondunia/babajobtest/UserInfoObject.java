package com.example.coupondunia.babajobtest;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@ParseClassName("userInfo")
public class UserInfoObject extends ParseObject implements Serializable {

    public boolean attendance;
    public String userName;
    public String age;
    public String location;
    public String userId;
    public String contactNumber;
    public String dates;

    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

    public UserInfoObject() {
    }


    public void setUserName(String name) {
        put(AddUserActivity.USER_NAME, name);
    }

    public String getUserName() {
        this.userName = getString(AddUserActivity.USER_NAME);
        return getString(AddUserActivity.USER_NAME);
    }

    public void setUserAge(String age) {
        put(AddUserActivity.USER_AGE, age);
    }

    public String getUserAge() {
        this.age = getString(AddUserActivity.USER_AGE);
        return getString(AddUserActivity.USER_AGE);
    }

    public void setUserLocation(String location) {
        put(AddUserActivity.USER_LOCATION, location);
    }

    public String getUserLocation() {
        this.location = getString(AddUserActivity.USER_LOCATION);
        return getString(AddUserActivity.USER_LOCATION);
    }

    public void setuserAttendance(String date) {
        put(AddUserActivity.USER_DATE, date);
    }

    public String getUserDate() {
        this.dates = getString(AddUserActivity.USER_DATE);
        return getString(AddUserActivity.USER_DATE);
    }

    public void setUserContactNumber(String number) {
        put(AddUserActivity.USER_CONTACT_NUMBER, number);
    }

    public String getUserContactNumber() {
        this.contactNumber = getString(AddUserActivity.USER_CONTACT_NUMBER);
        return getString(AddUserActivity.USER_CONTACT_NUMBER);
    }

    public void setUserQrcode(ParseFile qrCode) {
        put(AddUserActivity.USER_QRCODE, qrCode);
    }

    public ParseFile getQrCodeFile() {
        return getParseFile(AddUserActivity.USER_QRCODE);
    }

    /* public Bitmap getUserQrcode() {
        String encodedString = getString(AddUserActivity.USER_QRCODE);
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch (Exception e) {
            e.getMessage();
            return null;
        }
    }*/
}
