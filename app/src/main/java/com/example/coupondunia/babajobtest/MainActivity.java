package com.example.coupondunia.babajobtest;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView addEditAccount;
    ListView list;
    UserListAdapter adapter;
    private Calendar date;
    private DatePickerDialog datePickerDialog;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    String filterDate = null;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.list);


        addEditAccount = (TextView) findViewById(R.id.buttonAddDel);
        addEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddUserActivity.class));
            }
        });
        retrieveData();

        ((TextView) findViewById(R.id.btnMarkAttendance)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AttendanceForm.class));
            }
        });


        ((TextView) findViewById(R.id.header)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDate = null;
                retrieveData();
            }
        });

        try {
            Date d = sdf.parse("05/01/2016");
            date = Calendar.getInstance();
            date.setTime(d);
        }
        catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.datePicker)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker();
            }
        });
    }

    public void retrieveData() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        list.setVisibility(View.GONE);

        ParseQuery<UserInfoObject> query = ParseQuery.getQuery("userInfo");
        if(filterDate != null){
            query.whereContains(AddUserActivity.USER_DATE,filterDate);
        }
        query.findInBackground(new FindCallback<UserInfoObject>() {
            public void done(List<UserInfoObject> scoreList, ParseException e) {
                if (e == null) {
                    if (scoreList != null) {
                        list.setVisibility(View.VISIBLE);
                        adapter = new UserListAdapter(scoreList, MainActivity.this);
                        list.setAdapter(adapter);
                    }

                    progressDialog.dismiss();
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                }
                else {
                    list.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this,"Error: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void datepicker() {
        if (date == null) {
            date = Calendar.getInstance();
        }

        datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, monthOfYear);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                filterDate = sdf.format(date.getTime());
                retrieveData();
            }
        }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
        datePickerDialog.show();
    }
}
