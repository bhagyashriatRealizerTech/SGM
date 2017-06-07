package com.realizer.schoolgenie.managment.chat;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.realizer.schoolgenie.managment.DashboardActivity;
import com.realizer.schoolgenie.managment.R;
import com.realizer.schoolgenie.managment.chat.adapter.TeacherQueryModel1ListAdapter;
import com.realizer.schoolgenie.managment.chat.model.TeacherThreadListModel;
import com.realizer.schoolgenie.managment.exceptionhandler.ExceptionHandler;
import com.realizer.schoolgenie.managment.utils.Config;
import com.realizer.schoolgenie.managment.utils.FragmentBackPressedListener;
import com.realizer.schoolgenie.managment.utils.OnTaskCompleted;
import com.realizer.schoolgenie.managment.utils.QueueListModel;
import com.realizer.schoolgenie.managment.utils.Singlton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Created by Win on 11/26/2015.
 */
public class TeacherThreadListFragment extends Fragment implements OnTaskCompleted, FragmentBackPressedListener {

    ListView lsttname;
    MessageResultReceiver resultReceiver;
    MenuItem search,done;
    ArrayList<TeacherThreadListModel> chat ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View rootView = inflater.inflate(R.layout.teacher_queries_list_layout, container, false);

        ((DashboardActivity) getActivity()).getSupportActionBar().setTitle(Config.actionBarTitle("Communication", getActivity()));
        ((DashboardActivity) getActivity()).getSupportActionBar().show();

        lsttname = (ListView) rootView.findViewById(R.id.lsttname);
        setHasOptionsMenu(true);

        chat = new ArrayList<>();

        getThreadList();


        Singlton obj = Singlton.getInstance();
        resultReceiver = new MessageResultReceiver(null);
        obj.setResultReceiver(resultReceiver);



        lsttname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TeacherThreadListModel o = (TeacherThreadListModel) lsttname.getItemAtPosition(position);

                    Bundle bundle = new Bundle();
                    bundle.putString("USERID",o.getUid() );

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    TeacherMessageCenterFragment fragment = new TeacherMessageCenterFragment();
                    Singlton.setSelectedFragment(fragment);
                    fragment.setArguments(bundle);
                    transaction.replace(R.id.frame_layout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();


            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_main, menu);
        done = menu.findItem(R.id.action_done);
        done.setVisible(false);
        search = menu.findItem(R.id.action_search);
        search.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onTaskCompleted(String s, QueueListModel queueListModel) {
        ArrayList<TeacherThreadListModel> temp = new ArrayList<>();

        Collections.sort(temp, new ChatNoCaseComparator());

    }

    @Override
    public void onFragmentBackPressed() {
        Singlton.getMainFragment().getActivity().finish();
        Intent i = new Intent(getActivity(),DashboardActivity.class);
        startActivity(i);
    }


    //Update UI
    class UpdateUI implements Runnable {
        String update;

        public UpdateUI(String update,Bundle bundle) {

            this.update = update;
        }

        public void run() {

            if(update.equals("RecieveMessage")) {

                chat.add(new TeacherThreadListModel());

                    if(chat.size()!=0)
                    {
                        lsttname.setAdapter(new TeacherQueryModel1ListAdapter(getActivity(), chat));
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
                getActivity().runOnUiThread(new UpdateUI("SendMessageMessage",null));
            }

        }
    }

    public void  getThreadList()
        {
            //Call API To get Thread List

            if(chat.size()!=0)
            {

                lsttname.setAdapter(new TeacherQueryModel1ListAdapter(getActivity(), chat));
            }
        }

    @Override
    public void onResume() {
        super.onResume();

        if(search != null)
            search.setVisible(false);
        if(done != null)
            done.setVisible(false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public class ChatNoCaseComparator implements Comparator<TeacherThreadListModel> {
        public int compare(TeacherThreadListModel s1, TeacherThreadListModel s2) {
            return s2.getSenddate().compareTo(s1.getSenddate());
        }
    }
}
