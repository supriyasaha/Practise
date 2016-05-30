package com.example.coupondunia.babajobtest;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AttendanceForm extends AppCompatActivity {

    private FormAdapter adapter;
    private ListView list;
    private String currentDate;
    private List<UserInfoObject> userList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_form);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        currentDate = df.format(new Date());

        list = (ListView) findViewById(R.id.list);
        retrieveData();

        (findViewById(R.id.saveBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAttendance();
                finish();
            }
        });
    }


    public class FormAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;


            if (convertView == null) {
                convertView = LayoutInflater.from(AttendanceForm.this).inflate(R.layout.attendence_list_layout, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.userId = (TextView) convertView.findViewById(R.id.userId);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.attendance);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            final UserInfoObject userInfo = userList.get(position);
            final String dates = userInfo.getUserDate();


            holder.name.setText(userInfo.getUserName());
            holder.userId.setText(userInfo.getObjectId());

            if (userInfo.attendance) {
                holder.checkBox.setChecked(true);
            }
            else {
                if (dates != null && dates.contains(currentDate)) {
                    userInfo.attendance = true;
                    holder.checkBox.setChecked(true);
                }
                else {
                    userInfo.attendance = false;
                    holder.checkBox.setChecked(false);
                }
            }

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userInfo.attendance){
                        userInfo.attendance = false;
                    }else{
                        userInfo.attendance = true;
                    }
                }
        });
            return convertView;
        }

        public class ViewHolder {
            TextView name, userId;
            CheckBox checkBox;
        }
    }

    public void retrieveData() {
        ParseQuery<UserInfoObject> query = ParseQuery.getQuery("userInfo");
        query.findInBackground(new FindCallback<UserInfoObject>() {
            public void done(List<UserInfoObject> scoreList, ParseException e) {
                if (e == null) {
                    if (scoreList != null && scoreList.size() > 0) {
                        userList = scoreList;
                        adapter = new FormAdapter();
                        list.setAdapter(adapter);
                    }

                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void updateAttendance() {
        for (UserInfoObject userInfo : userList) {
            String dates = userInfo.getUserDate();
            if (userInfo.attendance) {
                if (dates == null) {
                    dates = currentDate;
                }
                else if (!dates.contains(currentDate)) {
                    dates = dates + "," + currentDate;
                }
            }
            else {

                if (dates != null && dates.contains(currentDate)) {
                    dates = dates.replace(currentDate, "");
                }
            }

            userInfo.dates = dates;
        }

        ParseQuery<UserInfoObject> query = ParseQuery.getQuery("userInfo");

        query.findInBackground(new FindCallback<UserInfoObject>() {
            public void done(List<UserInfoObject> scoreList, ParseException e) {
                if (e == null) {
                    if (scoreList != null && scoreList.size() > 0) {
                        for (int i = 0; i < scoreList.size(); i++) {
                            UserInfoObject userInfoObject = scoreList.get(i);
                            if(userList.get(i).dates != null) {
                                userInfoObject.setuserAttendance(userList.get(i).dates);
                            }
                            userInfoObject.saveInBackground();
                        }
                    }
                    Log.d("score", "Retrieved " + scoreList.size() + " scores");
                }
                else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }


}


