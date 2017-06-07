package com.realizer.schoolgenie.managment.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.realizer.schoolgenie.managment.chat.model.AddedContactModel;
import com.realizer.schoolgenie.managment.view.ProgressWheel;


import java.util.ArrayList;
import java.util.List;

import static com.realizer.schoolgenie.managment.R.styleable.ProgressWheel;

/**
 * Created by Bhagyashri on 4/2/2016.
 */
public class Singlton {

    private static Singlton _instance;
    public static ResultReceiver resultReceiver;
    public static Context context;
    public static Fragment fragment;
    public static Fragment mainFragment;
    public static Intent autoserviceIntent = null;
    public static Intent manualserviceIntent = null;
    public static ProgressWheel initiateChat = null;
    public static ProgressWheel messageCenter = null;
    public static Activity activity = null;
    public static ArrayList<AddedContactModel> selectedStudentList = new ArrayList<>();
    public static boolean isDonclick = Boolean.FALSE;
    public static ArrayList<AddedContactModel> selectedStudeonBackKeyPress = new ArrayList<>();
    private Singlton()
    {

    }

    public static Singlton getInstance()
    {
        if (_instance == null)
        {
            _instance = new Singlton();
        }
        return _instance;
    }



    public static ResultReceiver getResultReceiver() {
        return resultReceiver;
    }

    public static void setResultReceiver(ResultReceiver resultReceiver) {
        Singlton.resultReceiver = resultReceiver;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Singlton.context = context;
    }

    public static Fragment getSelectedFragment() {
        return fragment;
    }

    public static void setSelectedFragment(Fragment fragment) {
        Singlton.fragment = fragment;
    }

    public static Fragment getMainFragment() {
        return mainFragment;
    }

    public static void setMainFragment(Fragment mainFragment) {
        Singlton.mainFragment = mainFragment;
    }

    public static Intent getAutoserviceIntent() {
        return autoserviceIntent;
    }

    public static void setAutoserviceIntent(Intent autoserviceIntent) {
        Singlton.autoserviceIntent = autoserviceIntent;
    }

    public static Intent getManualserviceIntent() {
        return manualserviceIntent;
    }

    public static void setManualserviceIntent(Intent manualserviceIntent) {
        Singlton.manualserviceIntent = manualserviceIntent;
    }

    public static com.realizer.schoolgenie.managment.view.ProgressWheel getInitiateChat() {
        return initiateChat;
    }

    public static void setInitiateChat(com.realizer.schoolgenie.managment.view.ProgressWheel initiateChat) {
        Singlton.initiateChat = initiateChat;
    }

    public static com.realizer.schoolgenie.managment.view.ProgressWheel getMessageCenter() {
        return messageCenter;
    }

    public static void setMessageCenter(com.realizer.schoolgenie.managment.view.ProgressWheel messageCenter) {
        Singlton.messageCenter = messageCenter;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Singlton.activity = activity;
    }


    public static ArrayList<AddedContactModel> getSelectedStudentList() {
        return selectedStudentList;
    }

    public static void setSelectedStudentList(ArrayList<AddedContactModel> selectedStudentList) {
        Singlton.selectedStudentList = selectedStudentList;
    }

    public static boolean isDonclick() {
        return isDonclick;
    }

    public static void setIsDonclick(boolean isDonclick) {
        Singlton.isDonclick = isDonclick;
    }

    public static ArrayList<AddedContactModel> getSelectedStudeonBackKeyPress() {
        return selectedStudeonBackKeyPress;
    }

    public static void setSelectedStudeonBackKeyPress(ArrayList<AddedContactModel> selectedStudeonBackKeyPress) {
        Singlton.selectedStudeonBackKeyPress = selectedStudeonBackKeyPress;
    }
}

