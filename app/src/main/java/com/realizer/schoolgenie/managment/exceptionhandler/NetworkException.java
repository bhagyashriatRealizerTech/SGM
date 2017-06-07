package com.realizer.schoolgenie.managment.exceptionhandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.realizer.schoolgenie.managment.exceptionhandler.model.ExceptionModel;
import com.realizer.schoolgenie.managment.mailsender.MailSender;
import com.realizer.schoolgenie.managment.utils.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Bhagyashri on 11/17/2016.
 */
public class NetworkException {


    public static void insertNetworkException(Context myContext, String stackTrace) {
    SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(myContext);
    ExceptionModel obj = new ExceptionModel();
    obj.setUserId(sharedpreferences.getString("UidName",""));
    obj.setExceptionDetails(stackTrace.toString());
    obj.setDeviceModel(Build.MODEL);
    obj.setAndroidVersion(Build.VERSION.SDK);
    obj.setApplicationSource("Teacher");
    obj.setDeviceBrand(Build.BRAND);

    SimpleDateFormat df1 = new SimpleDateFormat("dd MMM hh:mm:ss a");
    String date = df1.format(Calendar.getInstance().getTime());


    if(Config.isConnectingToInternet(myContext))
        sendEmail(obj);


  }


    public static void sendEmail(final ExceptionModel obj)
    {
        new Thread(new Runnable() {

            public void run() {

                try {

                    String messageContent = "Application Source: "+obj.getApplicationSource()
                            +"\nDevice Model: "+obj.getDeviceModel()+"\nAndroid Version: "+obj.getAndroidVersion()
                            +"\nDevice Brand: "+obj.getDeviceBrand()+"\nUserID: "+obj.getUserId()
                            +"\nException: "+obj.getExceptionDetails();

                    String TO = "bhagyashri.salgare@realizertech.com,sachin.shinde@realizertech.com";

                    MailSender sender = new MailSender("realizertech1@gmail.com","realizer@15");

                   // sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");

                    sender.sendMail("Critical Network Error: Management App",messageContent,"realizertech1@gmail.com",TO);

                } catch (Exception e) {

                    Log.d("Exception Mail",e.toString());
                   // Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

                }

                               }

        }).start();
    }
}
