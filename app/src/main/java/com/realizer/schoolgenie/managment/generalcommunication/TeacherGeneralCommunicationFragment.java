package com.realizer.schoolgenie.managment.generalcommunication;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

import com.realizer.schoolgenie.managment.DashboardActivity;
import com.realizer.schoolgenie.managment.R;
import com.realizer.schoolgenie.managment.exceptionhandler.ExceptionHandler;
import com.realizer.schoolgenie.managment.generalcommunication.adapter.TeacherGeneralCommunicationListAdapter;
import com.realizer.schoolgenie.managment.generalcommunication.model.TeacherGeneralCommunicationListModel;
import com.realizer.schoolgenie.managment.utils.Config;
import com.realizer.schoolgenie.managment.utils.FragmentBackPressedListener;
import com.realizer.schoolgenie.managment.utils.Singlton;

import java.util.ArrayList;

/**
 * Created by Win on 11/25/2015.
 */
public class TeacherGeneralCommunicationFragment extends Fragment implements FragmentBackPressedListener {

    TextView noDataText;;
    FloatingActionButton txtnew;
    MenuItem search,done;
    ArrayList<TeacherGeneralCommunicationListModel> msg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View rootView = inflater.inflate(R.layout.teacher_generalcommunication_layout, container, false);

        ((DashboardActivity) getActivity()).getSupportActionBar().setTitle(Config.actionBarTitle("Alerts", getActivity()));
        ((DashboardActivity) getActivity()).getSupportActionBar().show();

        setHasOptionsMenu(true);
        txtnew = (FloatingActionButton) rootView.findViewById(R.id.txtnewcommunication);
        noDataText = (TextView)rootView.findViewById(R.id.tvNoDataMsg);
         msg = new ArrayList<>();

        final ListView listHoliday = (ListView) rootView.findViewById(R.id.lsttgeneralcommunication);
        if(msg.size()>0) {
            listHoliday.setVisibility(View.VISIBLE);
            noDataText.setVisibility(View.GONE);
            listHoliday.setAdapter(new TeacherGeneralCommunicationListAdapter(getActivity(), msg));
        }
        else
        {
            listHoliday.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);
        }
        listHoliday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = listHoliday.getItemAtPosition(position);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

                TeacherGeneralCommunicationListModel gcommunication = (TeacherGeneralCommunicationListModel) o;
                TeacherGcommunicationDetailFragment fragment = new TeacherGcommunicationDetailFragment();
                Singlton.setSelectedFragment(fragment);
                Bundle bundle = new Bundle();
                bundle.putString("CategoryName",gcommunication.getCategory());
                bundle.putString("AlertDate",Config.getMediumDate(gcommunication.getAnnouncementTime()));
                bundle.putString("TeacherName",preferences.getString("DisplayName",""));
                bundle.putString("AlertText",gcommunication.getAnnouncementText());
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });



        txtnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TeacherGCommunicationNewFragment fragment = new TeacherGCommunicationNewFragment();
                Singlton.setSelectedFragment(fragment);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame_layout,fragment);
                fragmentTransaction.commit();
            }
        });
        return rootView;
    }
    private void GetGeneralCommunicationList()
    {
        //Get All Messages for User
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
    public void onResume() {
        super.onResume();

        if(done != null)
        done.setVisible(false);
        if(search != null)
        search.setVisible(false);

            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onFragmentBackPressed() {
        Singlton.getMainFragment().getActivity().finish();
        Intent i = new Intent(getActivity(),DashboardActivity.class);
        startActivity(i);
    }
}
