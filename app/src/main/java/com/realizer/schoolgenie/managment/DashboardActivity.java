package com.realizer.schoolgenie.managment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.realizer.schoolgenie.managment.chat.TeacherThreadListFragment;
import com.realizer.schoolgenie.managment.exceptionhandler.ExceptionHandler;
import com.realizer.schoolgenie.managment.generalcommunication.TeacherGeneralCommunicationFragment;
import com.realizer.schoolgenie.managment.utils.FragmentBackPressedListener;
import com.realizer.schoolgenie.managment.utils.ImageStorage;
import com.realizer.schoolgenie.managment.utils.Singlton;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    TextView userName;
    ImageView userImage;
    TextView userInitials;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Singlton.setContext(DashboardActivity.this);
        Singlton.setActivity(DashboardActivity.this);
        overridePendingTransition(0,0);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideSoftKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                hideSoftKeyboard();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                hideSoftKeyboard();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);

        userName = (TextView)header.findViewById(R.id.txt_user_name);
        userImage = (ImageView) header.findViewById(R.id.img_user_image);
        userInitials = (TextView) header.findViewById(R.id.img_user_text_image);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName.setText(preferences.getString("DisplayName",""));

        String urlString = preferences.getString("ThumbnailID","");
        Log.d("Image URL",urlString);

        if(!urlString.isEmpty()){
            userImage.setVisibility(View.VISIBLE);
            userInitials.setVisibility(View.GONE);
            ImageStorage.setThumbnail(userImage,urlString);
        }
        else {
            userImage.setVisibility(View.GONE);
            userInitials.setVisibility(View.VISIBLE);
            String name[]=userName.getText().toString().split(" ");
            String fname = name[0].trim().toUpperCase().charAt(0)+"";
            if(name.length>1)
            {

                String lname = name[1].trim().toUpperCase().charAt(0)+"";
                userInitials.setText(fname+lname);
            }
            else
                userInitials.setText(fname);
        }

                /* Display First Fragment at Launch*/
        navigationView.setCheckedItem(R.id.nav_teacher);
        Fragment frag = new TeacherActivityReportFragment();
        Singlton.setSelectedFragment(frag);
        Singlton.setMainFragment(frag);
        if (frag != null)
        {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, frag).commit();

        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_teacher) {
            fragment = new TeacherActivityReportFragment();
            // Handle the camera action
        } else if (id == R.id.nav_student) {
            fragment = new StudentAttendanceReport();

        } else if (id == R.id.nav_message) {

            fragment = new TeacherThreadListFragment();
        } else if (id == R.id.nav_alert) {

            fragment = new TeacherGeneralCommunicationFragment();
        } else if (id == R.id.nav_logout) {

            final SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = sharedpreferences.edit();
            edit.putString("Login", "true");
            edit.commit();
            Intent intent = new Intent(DashboardActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (fragment != null)
        {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, fragment).commit();

        }

        Singlton.setSelectedFragment(fragment);
        Singlton.setMainFragment(fragment);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Lock Drawer Avoid opening it
     */
    public void lockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * UnLockDrawer and allow opening it
     */
    public void unlockDrawer() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void showMyActionBar() {
        getSupportActionBar().show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if(Singlton.getSelectedFragment() instanceof TeacherActivityReportFragment)
        {
            //moveTaskToBack(true);
            finish();
        }
        else if (Singlton.getSelectedFragment() != null && Singlton.getSelectedFragment() instanceof FragmentBackPressedListener) {
            ((FragmentBackPressedListener) Singlton.getSelectedFragment()).onFragmentBackPressed();
        }

        hideSoftKeyboard();
    }

}
