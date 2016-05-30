package com.example.coupondunia.babajobtest;


import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText firstName, age, contact, location;
    private TextView generateQRCode, btnAddAccount, btnUpdateAccount;
    private ImageView qrCodeImage;
    private String firstname, agevalue, locationvalue, phnNo;
    private Bitmap bitmap;
    Bundle bundle = null;
    UserInfoObject userInfo = null;
    public String objectId = null;


    public static final String USER_NAME = "userName";
    public static final String USER_AGE = "age";
    public static final String USER_EMAIL_ADDRESS = "emailAddress";
    public static final String USER_LOCATION = "location";
    public static final String USER_CONTACT_NUMBER = "contactNumber";
    public static final String USER_QRCODE = "file";
    public static final String USER_ID = "userId";
    public static final String USER_DATE = "date";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user_activity);
        processBundle();

        firstName = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        contact = (EditText) findViewById(R.id.contactNumber);
        location = (EditText) findViewById(R.id.btnlocation);
        qrCodeImage = (ImageView) findViewById(R.id.qrCodeImage);

        generateQRCode = (TextView) findViewById(R.id.btnQRCode);
        generateQRCode.setOnClickListener(this);
        btnAddAccount = (TextView) findViewById(R.id.btnAddAccount);
        btnAddAccount.setOnClickListener(this);
        btnUpdateAccount = (TextView) findViewById(R.id.btnUpdateAccount);
        btnUpdateAccount.setOnClickListener(this);

        if (userInfo != null) {
            firstName.setText(!TextUtils.isEmpty(firstname) ? firstname : "");
            age.setText(!TextUtils.isEmpty(agevalue) ? agevalue : "");
            contact.setText(!TextUtils.isEmpty(phnNo) ? phnNo : "");
            location.setText(!TextUtils.isEmpty(locationvalue) ? locationvalue : "");
        }

        if (bundle != null && userInfo != null) {
            btnUpdateAccount.setVisibility(View.VISIBLE);
            btnAddAccount.setVisibility(View.GONE);
        }
        else {
            btnAddAccount.setVisibility(View.VISIBLE);
            btnUpdateAccount.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnQRCode:
                getQrCOdeDetails();
                break;
            case R.id.btnAddAccount:
                if (bitmap != null) {
                    uploadUserData(bitmap);
                    finish();
                }
                else {
                    Toast.makeText(this, "Please generate a qrcode to continue", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnUpdateAccount:
                if (bitmap != null) {
                    updateAccount();
                    finish();
                }
                else {
                    Toast.makeText(this, "Please generate a qrcode to continue", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void processBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            userInfo = (UserInfoObject) bundle.getSerializable("userInfo");
            firstname = userInfo.userName;
            locationvalue = userInfo.location;
            phnNo = userInfo.contactNumber;
            agevalue = userInfo.age;
            objectId = bundle.getString("objectId");
        }
    }

    public void getQrCOdeDetails() {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        firstname = !TextUtils.isEmpty(firstName.getText()) ? firstName.getText().toString() : "";
        agevalue = (!TextUtils.isEmpty(age.getText()) ? age.getText().toString() : "");
        phnNo = (!TextUtils.isEmpty(contact.getText()) ? contact.getText().toString() : "");
        locationvalue = (!TextUtils.isEmpty(location.getText()) ? location.getText().toString() : "");

        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(this, "Enter candidates name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(agevalue)) {
            Toast.makeText(this, "Enter candidates age", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(phnNo)) {
            Toast.makeText(this, "Enter candidates contact number", Toast.LENGTH_SHORT).show();
            return;
        }else if(phnNo.length() != 10) {
            Toast.makeText(this, "Enter valid contact number", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(locationvalue)){
            Toast.makeText(this, "Enter candidates location", Toast.LENGTH_SHORT).show();
            return;
        }

        String inputText = (firstName.getHint().toString() + "=" + firstname) +
                (age.getHint().toString() + "=" + (!TextUtils.isEmpty(age.getText()) ? age.getText().toString() : "")) +
                (location.getHint().toString() + "=" + locationvalue) +
                (contact.getHint().toString() + "=" + phnNo);
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(inputText,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            bitmap = qrCodeEncoder.encodeAsBitmap();
            qrCodeImage.setImageBitmap(bitmap);

        }
        catch (WriterException e) {
            e.printStackTrace();
        }

    }

    public void uploadUserData(Bitmap bitmap) {

        String userId = UUID.randomUUID().toString();
        if (bitmap != null) {

            byte[] data = getStringImage(bitmap);
            ParseFile file = new ParseFile("resume.txt", data);

            file.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Saved successfully.
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // The save failed.
                        Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                        Log.d(getClass().getSimpleName(), "User update error: " + e);
                    }
                }

            });

            UserInfoObject userInfo = new UserInfoObject();
//            userInfo.setUserName(firstname);
//            userInfo.setUserAge(agevalue);
//            userInfo.setUserContactNumber(phnNo);
//            userInfo.setUserLocation(locationvalue);
//            userInfo.setUserQrcode(file);

            createUserInfoObject(userInfo).saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Saved successfully.
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // The save failed.
                        Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                        Log.d(getClass().getSimpleName(), "User update error: " + e);
                    }
                }

            });


        }
    }

    public byte[] getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
    }

    public void updateAccount() {
        String userId = objectId;

        ParseQuery<UserInfoObject> query = ParseQuery.getQuery("userInfo");

        query.getInBackground(objectId, new GetCallback<UserInfoObject>() {
            public void done(UserInfoObject gameScore, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    createUserInfoObject(gameScore).saveInBackground();
                }
            }
        });

    }

    public UserInfoObject createUserInfoObject(UserInfoObject userInfo) {
        userInfo.setUserName(firstname);
        userInfo.setUserAge(agevalue);
        userInfo.setUserContactNumber(phnNo);
        userInfo.setUserLocation(locationvalue);

        return userInfo;
    }


}
