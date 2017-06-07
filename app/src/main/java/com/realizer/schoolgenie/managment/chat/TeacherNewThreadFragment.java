package com.realizer.schoolgenie.managment.chat;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.realizer.schoolgenie.managment.DashboardActivity;
import com.realizer.schoolgenie.managment.R;
import com.realizer.schoolgenie.managment.chat.adapter.TeacherQueryAddedContactListAdapter;
import com.realizer.schoolgenie.managment.chat.adapter.TeacherQueryAutoCompleteListAdapter;
import com.realizer.schoolgenie.managment.chat.asynctask.TeacherQueryAsyncTaskPost;
import com.realizer.schoolgenie.managment.chat.model.AddedContactModel;
import com.realizer.schoolgenie.managment.chat.model.TeacherQuerySendModel;
import com.realizer.schoolgenie.managment.chat.model.TeacherThreadListModel;
import com.realizer.schoolgenie.managment.exceptionhandler.ExceptionHandler;
import com.realizer.schoolgenie.managment.utils.ChatSectionIndexer;
import com.realizer.schoolgenie.managment.utils.Config;
import com.realizer.schoolgenie.managment.utils.FragmentBackPressedListener;
import com.realizer.schoolgenie.managment.utils.OnTaskCompleted;
import com.realizer.schoolgenie.managment.utils.QueueListModel;
import com.realizer.schoolgenie.managment.utils.Singlton;
import com.realizer.schoolgenie.managment.view.ProgressWheel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Win on 11/26/2015.
 */
public class TeacherNewThreadFragment extends Fragment implements OnTaskCompleted, FragmentBackPressedListener {

    //ImageView selectStudent;
   // EditText autocomplteTextView;
    EditText message,subject,messageTo;
    String std=null,div=null;
    MenuItem search,done;
    ProgressWheel loading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View rootView = inflater.inflate(R.layout.teacher_query_new_message, container, false);
        setHasOptionsMenu(true);

        ((DashboardActivity) getActivity()).getSupportActionBar().setTitle(Config.actionBarTitle("Reply", getActivity()));
        ((DashboardActivity) getActivity()).getSupportActionBar().show();

         initiateView(rootView);
        Config.hideSoftKeyboardWithoutReq(getActivity(), message);
        return rootView;
    }


    public void initiateView(View view)
    {

        messageTo = (EditText) view.findViewById(R.id.edt_select_contact);
        subject = (EditText)view.findViewById(R.id.edt_title);
        message = (EditText)view.findViewById(R.id.edtnewalerts);
        loading = (ProgressWheel)view.findViewById(R.id.loading);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        done = menu.findItem(R.id.action_done);
        search = menu.findItem(R.id.action_search);
        search.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_done:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onTaskCompleted(String s, QueueListModel queueListModel) {


        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String stdC=sharedpreferences.getString("STANDARD", "");
        String divC = sharedpreferences.getString("DIVISION", "");
        if(s.equals("true") && queueListModel.getType().equalsIgnoreCase("Query"))
        {
            loading.setVisibility(View.GONE);
            Singlton.setInitiateChat(null);
            TeacherThreadListFragment fragment = new TeacherThreadListFragment();
            Singlton.setSelectedFragment(fragment);
            Singlton.setMainFragment(fragment);
            Bundle bundle = new Bundle();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragment.setArguments(bundle);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frame_layout,fragment);
            fragmentTransaction.commit();
        }
        else {
            Config.hideSoftKeyboardWithoutReq(getActivity(), message);
        }
    }


    @Override
    public void onFragmentBackPressed() {
        Singlton.setSelectedFragment(Singlton.getMainFragment());
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(search != null)
            search.setVisible(false);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


}
