package com.realizer.schoolgenie.managment.chat;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.realizer.schoolgenie.managment.DashboardActivity;
import com.realizer.schoolgenie.managment.R;
import com.realizer.schoolgenie.managment.chat.adapter.TeacherQueryMessageCenterListAdapter;
import com.realizer.schoolgenie.managment.chat.asynctask.TeacherQueryAsyncTaskPost;
import com.realizer.schoolgenie.managment.chat.model.TeacherQuerySendModel;
import com.realizer.schoolgenie.managment.chat.model.TeacherQueryViewListModel;
import com.realizer.schoolgenie.managment.exceptionhandler.ExceptionHandler;
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
import java.util.Date;
import java.util.Timer;

/**
 * Created by Win on 11/26/2015.
 */
public class TeacherMessageCenterFragment extends Fragment implements AbsListView.OnScrollListener,OnTaskCompleted, FragmentBackPressedListener {

    Timer timer;
    Parcelable state;
    int currentPosition;
    ListView lsttname;
    int qid;
    int mCurrentX ;
    int  mCurrentY;
    TextView send;
    EditText msg;
    int lstsize;
    TeacherQueryMessageCenterListAdapter adapter;
    Context context;
    MessageResultReceiver resultReceiver;
    String sname,uidname,thumbnailUrl,senderName;
    ProgressWheel loading;
    ArrayList<TeacherQueryViewListModel> teachernames;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View rootView = inflater.inflate(R.layout.teacher_queryview_layout, container, false);
        lsttname = (ListView) rootView.findViewById(R.id.lstviewquery);
        msg = (EditText) rootView.findViewById(R.id.edtmsgtxt);
        send = (TextView) rootView.findViewById(R.id.btnSendText);
        loading = (ProgressWheel)rootView.findViewById(R.id.loading);

        Bundle b = getArguments();
        sname = b.getString("SENDERNAME");


        ((DashboardActivity) getActivity()).getSupportActionBar().setTitle(Config.actionBarTitle(sname, getActivity()));
        ((DashboardActivity) getActivity()).getSupportActionBar().show();

        Singlton obj = Singlton.getInstance();
        resultReceiver = new MessageResultReceiver(null);
        obj.setResultReceiver(resultReceiver);


        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
         uidname = sharedpreferences.getString("UidName", "");
         thumbnailUrl = sharedpreferences.getString("ThumbnailID", "");
         senderName = sharedpreferences.getString("DisplayName", "");

        GetQuery();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.hideSoftKeyboardWithoutReq(getActivity(),msg);
                if(msg.getText().length()!=0)
               {
                   loading.setVisibility(View.VISIBLE);
                   Singlton.setMessageCenter(loading);
                    Bundle b = getArguments();
                    String uidstud = b.getString("USERID");
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy h:mm:ss a");
                    String date = df.format(calendar.getTime());
                   Date sendDate =  new Date();
                   try {
                       sendDate = df.parse(date);
                   } catch (ParseException e) {
                       e.printStackTrace();
                   }



                            if(isConnectingToInternet()) {
                                TeacherQuerySendModel obj =new TeacherQuerySendModel();
                                TeacherQueryAsyncTaskPost asyncobj = new TeacherQueryAsyncTaskPost(obj, getActivity(), TeacherMessageCenterFragment.this, "true");
                                asyncobj.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                            else {
                                resultReceiver.send(200, null);
                            }

                }
            }
        });

        return rootView;
    }


    private void GetQuery()
    {
        //Call API to get all Message for thread
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {



    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        mCurrentX = view.getScrollX();
        mCurrentY = view.getScrollY();
        currentPosition = lsttname.getSelectedItemPosition();
        Log.d("Position", "" + currentPosition);

    }


    @Override
    public void onTaskCompleted(String s, QueueListModel queueListModel) {
        if(s.equals("true") && queueListModel.getType().equalsIgnoreCase("Query"))
        {

                    resultReceiver.send(200,null);


        }
        else {
            Bundle b = this.getArguments();
            String uid = b.getString("USERID");
            ArrayList<TeacherQueryViewListModel> results = new ArrayList<>();
            ArrayList<TeacherQuerySendModel> qlst =new ArrayList<>();//set api result
            String tp="AM";

            for(int i=0;i<qlst.size();i++)
            {
                TeacherQuerySendModel obj = qlst.get(i);
                TeacherQueryViewListModel tDetails = new TeacherQueryViewListModel();
                String datet[] = obj.getSentTime().split(" ");
                if(datet.length>2)
                    tp = datet[2];
                tDetails.setSenddate(datet[0]);
                String time[] = datet[1].split(":");
                String outtime = "";
                if(time.length>2)
                    outtime = "" + time[0] + ":" + time[1] + " " + tp;
                else if(time.length>1)
                    outtime = "" + time[0] + ":" + time[1];
                else if(time.length>0)
                    outtime = "" + time[0] ;
                tDetails.setTime(outtime);


                if(uid.equals(obj.getFrom()))
                    tDetails.setFlag("P");
                else
                    tDetails.setFlag("T");
                tDetails.setMsg(obj.getText());
                tDetails.setSendername(obj.getMsgSenderName());
                tDetails.setProfileImage(obj.getProfileImage());
                results.add(tDetails);
            }
            teachernames = new ArrayList<>();
            lstsize = teachernames.size();

            adapter = new TeacherQueryMessageCenterListAdapter(getActivity(),teachernames);
            lsttname.setAdapter(adapter);
            lsttname.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            lsttname.setFastScrollEnabled(true);
            lsttname.setScrollY(lsttname.getCount());
            lsttname.setSelection(lsttname.getCount() - 1);
            lsttname.smoothScrollToPosition(lsttname.getCount());
            lsttname.setOnScrollListener(this);

            Config.hideSoftKeyboardWithoutReq(getActivity(), msg);
        }

    }


    public boolean isConnectingToInternet(){

        ConnectivityManager connectivity =
                (ConnectivityManager) getActivity().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    @Override
    public void onFragmentBackPressed() {
        Singlton.setSelectedFragment(Singlton.getMainFragment());
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
    }

    class UpdateUI implements Runnable {
        String update;

        public UpdateUI(String update,Bundle bundle) {

            this.update = update;
        }

        public void run() {

            if(update.equals("RecieveMessage")) {

               teachernames.add(new TeacherQueryViewListModel());
                if(teachernames.size()!=lstsize)
                {
                    adapter = new TeacherQueryMessageCenterListAdapter(getActivity(), teachernames);
                    lsttname.setAdapter(adapter);
                    lsttname.setFastScrollEnabled(true);
                    lsttname.setScrollY(lsttname.getCount());
                    lsttname.setSelection(lsttname.getCount() - 1);
                    lsttname.smoothScrollToPosition(lsttname.getCount());
                    lstsize =  teachernames.size();
                }
            }

            else if(update.equals("SendMessageMessage")) {
                teachernames.add(new TeacherQueryViewListModel());

                if(teachernames.size() !=lstsize)
                {
                    adapter = new TeacherQueryMessageCenterListAdapter(getActivity(), teachernames);
                    lsttname.setAdapter(adapter);
                    lsttname.setFastScrollEnabled(true);
                    lsttname.setScrollY(lsttname.getCount());
                    lsttname.setSelection(lsttname.getCount() - 1);
                    lsttname.smoothScrollToPosition(lsttname.getCount());
                    lstsize =  teachernames.size();
                    loading.setVisibility(View.GONE);
                    Singlton.setMessageCenter(null);
                }
            }
        }
    }

    //Recive the result when new Message Arrives
    class MessageResultReceiver extends ResultReceiver
    {
        public MessageResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if(resultCode == 100){
                getActivity().runOnUiThread(new UpdateUI("RecieveMessage",resultData));
            }
            if(resultCode == 200){
                getActivity().runOnUiThread(new UpdateUI("SendMessageMessage",resultData));
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
