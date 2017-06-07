package com.realizer.schoolgenie.managment.generalcommunication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.realizer.schoolgenie.managment.DashboardActivity;
import com.realizer.schoolgenie.managment.R;
import com.realizer.schoolgenie.managment.exceptionhandler.ExceptionHandler;
import com.realizer.schoolgenie.managment.generalcommunication.adapter.TeacherGCommunicationNewListAdapter;
import com.realizer.schoolgenie.managment.generalcommunication.asynctask.TeacherGCommunicationAsyncTaskPost;
import com.realizer.schoolgenie.managment.generalcommunication.model.TeacherGCommunicationNewListModel;
import com.realizer.schoolgenie.managment.generalcommunication.model.TeacherGeneralCommunicationListModel;
import com.realizer.schoolgenie.managment.stddiv.TeacherMyClassDialogBoxActivity;
import com.realizer.schoolgenie.managment.utils.Config;
import com.realizer.schoolgenie.managment.utils.FragmentBackPressedListener;
import com.realizer.schoolgenie.managment.utils.OnTaskCompleted;
import com.realizer.schoolgenie.managment.utils.QueueListModel;
import com.realizer.schoolgenie.managment.utils.Singlton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Win on 11/25/2015.
 */
public class TeacherGCommunicationNewFragment extends Fragment implements OnTaskCompleted, FragmentBackPressedListener {

    EditText edtmsg,edtTitle;
    Spinner stdDiv;
    ArrayList<TeacherGCommunicationNewListModel> teachernames;
    String scode;
    List stdList;
    MenuItem search;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View rootView = inflater.inflate(R.layout.teacher_newgcommunication_layout, container, false);

       // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ((DashboardActivity) getActivity()).getSupportActionBar().setTitle(Config.actionBarTitle("New Alert", getActivity()));
        ((DashboardActivity) getActivity()).getSupportActionBar().show();

        setHasOptionsMenu(true);

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
         scode= sharedpreferences.getString("SchoolCode", "");

        //populate list
        //teachernames = GetCategoryName();
        edtmsg = (EditText) rootView.findViewById(R.id.edtnewalerts);
        edtTitle = (EditText) rootView.findViewById(R.id.edt_title);
        stdDiv = (Spinner) rootView.findViewById(R.id.spinner_stddiv);
        Config.hideSoftKeyboardWithoutReq(getActivity(), edtmsg);

        setSpinnerData();
        stdDiv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    public void setSpinnerData()
    {
        //Call API to Get All Std Div

        stdList = new ArrayList<>();
        stdList.add("Select Standard");
        stdList.add("LKG");
        stdList.add("UKG");
        stdList.add("First");
        stdList.add("Second");
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity().getBaseContext(),android.R.layout.simple_spinner_item,stdList);
        stdDiv.setAdapter(adapter
        );
    }

    public List getDivList(String std){
        List div = new ArrayList();

        //Sort Div as per std given
        return div;
    }
    public void InsertData()
    {

        if(stdDiv.getSelectedItem().toString().equalsIgnoreCase("Select Standard") )
        {
            Config.alertDialog(Singlton.getContext(),"New Alert","Please Select Standard");
            //Toast.makeText(getActivity(), "Select Standard", Toast.LENGTH_SHORT).show();
        }
        else if(edtTitle.getText().toString().isEmpty())
        {
            //Toast.makeText(getActivity(), "Select Division", Toast.LENGTH_SHORT).show();
            Config.alertDialog(Singlton.getContext(),"New Alert","Please Enter Title");
        }
        else if(edtmsg.getText().toString().isEmpty())
        {
            Config.alertDialog(Singlton.getContext(),"New Alert","Please Enter Description");
           // Toast.makeText(getActivity(), "Enter Discription", Toast.LENGTH_SHORT).show();
        }
        else {
            Calendar calendar = Calendar.getInstance();
            int y = calendar.get(Calendar.YEAR);
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            SimpleDateFormat df1 = new SimpleDateFormat("hh:mm");

            SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sendby = sharedpreferences.getString("UidName", "");


            String date = df.format(calendar.getTime());
            String year = String.valueOf(y);
            String msg = edtmsg.getText().toString();
            String category = edtTitle.getText().toString();
            String time = df1.format(calendar.getTime());
            String std = stdDiv.getSelectedItem().toString();
            List div = getDivList(std);

            for (int i=0;i<div.size();i++) {
                if (Config.isConnectingToInternet(Singlton.getContext())) {
                    TeacherGeneralCommunicationListModel obj = new TeacherGeneralCommunicationListModel();
                    String scode = sharedpreferences.getString("SchoolCode", "");
                    obj.setSchoolCode(scode);
                    obj.setAcademicYr(year);
                    obj.setAnnouncementId(i);
                    obj.setAnnouncementText(msg);
                    obj.setAnnouncementTime(""+div.size());
                    obj.setCategory(category);
                    obj.setDivision(div.get(i).toString());
                    obj.setSentBy(sendby);
                  /*  TeacherGCommunicationAsyncTaskPost asyncobj = new TeacherGCommunicationAsyncTaskPost(obj, Singlton.getContext(), TeacherGCommunicationNewFragment.this, "false");
                    asyncobj.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);*/
                }
                else {
                    break;
                }
            }
        }
    }




    private ArrayList<TeacherGCommunicationNewListModel> GetCategoryName()
    {

        Bundle b = this.getArguments();
        //"PTA,,Parent Teacher Assosciation,,SD,,Sports Day_CA,,Cultural Activity_O,,Other_FDC,,Fancy Dress Competition"
        String[] categorylist = (b.getString("CategorNameList")).toString().split("_");
        ArrayList<TeacherGCommunicationNewListModel> results = new ArrayList<>();

        for(String category : categorylist)
        {
            String[] catName = category.toString().split(",,");
            TeacherGCommunicationNewListModel catDetails = new TeacherGCommunicationNewListModel();
            catDetails.setCatshortname(catName[0]);
            catDetails.setCatfullname(catName[1]);
            results.add(catDetails);
        }
        return results;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_main, menu);
        search = menu.findItem(R.id.action_search);
        search.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_done:
                Config.hideSoftKeyboardWithoutReq(getActivity(),edtmsg);
                InsertData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onTaskCompleted(String s, QueueListModel queueListModel) {

        long n=-1;
        if(s.equals("true") && queueListModel.getPriority().equalsIgnoreCase("Last"))
        {
                    TeacherGeneralCommunicationFragment fragment = new TeacherGeneralCommunicationFragment();
                    Singlton.setSelectedFragment(fragment);
                    Singlton.setMainFragment(fragment);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.frame_layout, fragment);
                    fragmentTransaction.commit();

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
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
