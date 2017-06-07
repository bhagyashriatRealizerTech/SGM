package com.realizer.schoolgenie.managment.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.realizer.schoolgenie.managment.R;
import com.realizer.schoolgenie.managment.chat.model.TeacherThreadListModel;
import com.realizer.schoolgenie.managment.utils.Config;
import com.realizer.schoolgenie.managment.utils.ImageStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * Created by Win on 11/20/2015.
 */
public class TeacherQueryModel1ListAdapter extends BaseAdapter {


    private static ArrayList<TeacherThreadListModel> pList;
    private LayoutInflater publicholidayDetails;
    private Context context1;
    boolean isImageFitToScreen;
    View convrtview;
    private String Currentdate;


    public TeacherQueryModel1ListAdapter(Context context, ArrayList<TeacherThreadListModel> dicatationlist) {
        pList = dicatationlist;
        publicholidayDetails = LayoutInflater.from(context);
        context1 = context;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        Currentdate = df.format(c.getTime());
    }

    @Override
    public int getCount() {
        return pList.size();
    }

    @Override
    public Object getItem(int position) {

        return pList.get(position);
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
            convertView = publicholidayDetails.inflate(R.layout.teacher_querygen_list_layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.txtthreadname);
            holder.unreadcount = (TextView) convertView.findViewById(R.id.txtunreadcount);
            holder.initial = (TextView) convertView.findViewById(R.id.txtinitial);
            holder.lastmsgtime = (TextView) convertView.findViewById(R.id.txtdate);
            holder.lstmsgsendername = (TextView) convertView.findViewById(R.id.txtsendername);
            holder.useImage = (ImageView)convertView.findViewById(R.id.img_user_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        String name[] = pList.get(position).getUname().trim().split(" ");


        if(!pList.get(position).getProfilrImage().isEmpty()){
            holder.useImage.setVisibility(View.VISIBLE);
            holder.initial.setVisibility(View.GONE);
            ImageStorage.setThumbnail(holder.useImage,pList.get(position).getProfilrImage());
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
        holder.lstmsgsendername.setText(pList.get(position).getSendername()+": "+pList.get(position).getLastMessage());
        if(Config.getDate(pList.get(position).getLastMsgDate(),"D").equalsIgnoreCase("Today"))
            holder.lastmsgtime.setText(Config.getDate(pList.get(position).getLastMsgDate(),"T"));
        else
            holder.lastmsgtime.setText(Config.getDate(pList.get(position).getLastMsgDate(), "D"));
        //holder.lastmsg.setText(pList.get(position).getLastMessage());

        if(pList.get(position).getUnreadCount() != 0)
        {
            holder.unreadcount.setVisibility(View.VISIBLE);
            holder.unreadcount.setText(""+pList.get(position).getUnreadCount());

        }
        else
        {
            holder.unreadcount.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {

       TextView name,unreadcount,initial,lastmsgtime,lstmsgsendername;
        ImageView useImage;

    }
}
