package com.example.coupondunia.babajobtest;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends BaseAdapter {

    List<UserInfoObject> userList;
    Context context;

    public UserListAdapter(List<UserInfoObject> userlist, Context context) {
        this.userList = userlist;
        this.context = context;
    }

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
        final UserInfoObject userInfo = userList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_info_id_card, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tvName);
            holder.location = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.age = (TextView) convertView.findViewById(R.id.tvAge);
            holder.contact = (TextView) convertView.findViewById(R.id.tvContactNumber);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText("Name:" + userInfo.getUserName());
        holder.age.setText("Age:" + userInfo.getUserAge());
        holder.location.setText("Location:" + userInfo.getUserLocation());
        holder.contact.setText("Contact:" + userInfo.getUserContactNumber());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("userInfo", userInfo);
                bundle.putString("objectId", userInfo.getObjectId());
                Intent intent = new Intent(context, AddUserActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView name, emailaddress, location, age, contact;
        ImageView qrCode;
    }
}
