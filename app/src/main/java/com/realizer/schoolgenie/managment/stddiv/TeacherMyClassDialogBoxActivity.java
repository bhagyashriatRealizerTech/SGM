package com.realizer.schoolgenie.managment.stddiv;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.realizer.schoolgenie.managment.R;
import com.realizer.schoolgenie.managment.chat.TeacherNewThreadFragment;
import com.realizer.schoolgenie.managment.exceptionhandler.ExceptionHandler;
import com.realizer.schoolgenie.managment.stddiv.adapter.TeacherMyClassListAdapter;
import com.realizer.schoolgenie.managment.stddiv.model.TeacherMyClassModel;
import com.realizer.schoolgenie.managment.utils.FragmentBackPressedListener;
import com.realizer.schoolgenie.managment.utils.Singlton;

import java.util.ArrayList;

/**
 * Created by shree on 12/4/2015.
 */
public class TeacherMyClassDialogBoxActivity extends DialogFragment implements FragmentBackPressedListener {
    ArrayList<TeacherMyClassModel> dailyClassList;
    ListView lstClassList;
    Button cancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        //DatabaseQueries qr = new DatabaseQueries(getActivity());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.teacher_myclass_dialog_layout, null);
        lstClassList =(ListView)view.findViewById(R.id.teacherclassname);
        cancel=(Button)view.findViewById(R.id.CancelList);
        Typeface face= Typeface.createFromAsset(getActivity().getAssets(), "fonts/font.ttf");
        cancel.setTypeface(face);
        dailyClassList = new ArrayList<>();
        getAllStdDiv();


        /*final Bundle b = this.getArguments();
        final int k = b.getInt("MYCLASS",0);*/
        lstClassList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = lstClassList.getItemAtPosition(position);
                TeacherMyClassModel obj = (TeacherMyClassModel)o;
                String std = obj.getStandard();
                String div= obj.getDivisioin();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("STANDARD",std);
                editor.putString("DIVISION",div);
                editor.commit();

                    RefreshMyClass(std,div);
                    dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Singlton.setSelectedFragment(Singlton.getMainFragment());
                dismiss();
            }
        });
        builder.setTitle("Select Class");
        builder.setView(view);
        return builder.create();
    }

    public void getAllStdDiv(){
        //Call API to get All std and Div
        TeacherMyClassModel obj = new TeacherMyClassModel();
        obj.setDivisioin("A");
        obj.setSrno("1");
        obj.setStandard("LKG");
        dailyClassList.add(obj);

        if(dailyClassList.size() > 0){
            lstClassList.setAdapter(new TeacherMyClassListAdapter(getActivity(), dailyClassList));
        }
    }

    public void RefreshMyClass(String std, String div)
    {

        TeacherNewThreadFragment fragment = new TeacherNewThreadFragment();
        Singlton.setSelectedFragment(fragment);
        Singlton.setMainFragment(fragment);
        Bundle bundle = new Bundle();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        bundle.putString("Std", std);
        bundle.putString("Div", div);
        fragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentBackPressed() {
        Singlton.setSelectedFragment(Singlton.getMainFragment());
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
    }
}
