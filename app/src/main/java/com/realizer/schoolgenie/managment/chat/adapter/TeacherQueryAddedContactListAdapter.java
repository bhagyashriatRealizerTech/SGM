package com.realizer.schoolgenie.managment.chat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.realizer.schoolgenie.managment.R;
import com.realizer.schoolgenie.managment.chat.model.AddedContactModel;
import com.realizer.schoolgenie.managment.utils.GetImages;
import com.realizer.schoolgenie.managment.utils.ImageStorage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;



/**
 * Created by Win on 11/20/2015.
 */
public class TeacherQueryAddedContactListAdapter extends BaseAdapter {


    private static ArrayList<AddedContactModel> contactList;
    private LayoutInflater addedContact;
    private Context context1;
    boolean isImageFitToScreen;
    View convrtview;
    private String Currentdate;


    public TeacherQueryAddedContactListAdapter(Context context, ArrayList<AddedContactModel> contactList1) {
        contactList = contactList1;
        addedContact = LayoutInflater.from(context);
        context1 = context;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        Currentdate = df.format(c.getTime());
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {

        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        convrtview = convertView;
        if (convertView == null) {
            convertView = addedContact.inflate(R.layout.teacher_query_added_contact_list_layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.txtFullName);
            holder.initial = (TextView) convertView.findViewById(R.id.txtinitial);
            holder.profilepic = (ImageView)convertView.findViewById(R.id.profile_image_view);
            holder.deleteContact = (TextView)convertView.findViewById(R.id.txtdelete);
            holder.deleteContact.setTag(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name[] = contactList.get(position).getUserName().trim().split(" ");
        if(!contactList.get(position).getProfileimage().isEmpty()){
            holder.profilepic.setVisibility(View.VISIBLE);
            holder.initial.setVisibility(View.GONE);
            ImageStorage.setThumbnail(holder.profilepic,contactList.get(position).getProfileimage());
        }
        else {
            char fchar = name[0].toUpperCase().charAt(0);
            char lchar = name[0].toUpperCase().charAt(0);
            for (int i = 0; i < name.length; i++) {
                if (!name[i].equals("") && i == 0)
                    fchar = name[i].toUpperCase().charAt(0);
                else if (!name.equals("") && i == (name.length - 1))
                    lchar = name[i].toUpperCase().charAt(0);

            }
            holder.initial.setText(fchar + "" + lchar);
        }
        String userName = "";

        for(int i=0;i<name.length;i++)
        {
            userName = userName+" "+name[i];
        }
        holder.name.setText(userName.trim());

        holder.deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (Integer)v.getTag();
                contactList.remove(pos);
               notifyDataSetChanged();
            }
        });


        return convertView;
    }

    static class ViewHolder {

       TextView name,initial,deleteContact;
       ImageView profilepic;

    }
}
