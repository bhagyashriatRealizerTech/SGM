package com.realizer.schoolgenie.managment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.realizer.schoolgenie.managment.exceptionhandler.ExceptionHandler;
import com.realizer.schoolgenie.managment.utils.Config;
import com.realizer.schoolgenie.managment.utils.FragmentBackPressedListener;
import com.realizer.schoolgenie.managment.utils.MyAxisLableFormatter;
import com.realizer.schoolgenie.managment.utils.MyAxisValueFormatter;
import com.realizer.schoolgenie.managment.utils.MyValueFormatter;
import com.realizer.schoolgenie.managment.utils.OnTaskCompleted;
import com.realizer.schoolgenie.managment.utils.QueueListModel;
import com.realizer.schoolgenie.managment.utils.Singlton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Win on 01-06-2017.
 */

public class StudentAttendanceReport extends Fragment implements OnTaskCompleted, FragmentBackPressedListener, OnChartValueSelectedListener {

    private BarChart mChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View rootView = inflater.inflate(R.layout.student_attendance_report, container, false);

        ((DashboardActivity) getActivity()).getSupportActionBar().setTitle(Config.actionBarTitle("Student Attendance", getActivity()));
        ((DashboardActivity) getActivity()).getSupportActionBar().show();

        mChart = (BarChart) rootView.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);


        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setLabelCount(10);
        mChart.getAxisRight().setEnabled(false);


        XAxis xLabels = mChart.getXAxis();
        xLabels.setGranularity(1f);
        xLabels.setDrawGridLines(false);
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("LKG-A");
        labels.add("LKG-B");
        labels.add("LKG-C");
        labels.add("UKG-A");
        labels.add("UKG-B");
        labels.add("UKG-C");

        xLabels.setValueFormatter(new MyAxisLableFormatter(labels));

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(10f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);



        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(
                    0,
                    new float[]{20f, 30f}));
        yVals1.add(new BarEntry(
                1,
                new float[]{30f, 40f}));
        yVals1.add(new BarEntry(
                2,
                new float[]{40f, 50f}));
        yVals1.add(new BarEntry(
                3,
                new float[]{50f, 40f}));
        yVals1.add(new BarEntry(
                4,
                new float[]{35f, 45f}));
        yVals1.add(new BarEntry(
                5,
                new float[]{25f, 45f}));

        BarDataSet set1;
            set1 = new BarDataSet(yVals1, "");
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Present", "Absent"});

            BarData data = new BarData(set1);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextSize(12f);
            data.setValueTextColor(Color.WHITE);

            mChart.setData(data);


        mChart.setFitBars(true);
        mChart.invalidate();
        return rootView;
    }


    @Override
    public void onFragmentBackPressed() {
        Singlton.getMainFragment().getActivity().finish();
        Intent i = new Intent(getActivity(),DashboardActivity.class);
        startActivity(i);
    }

    @Override
    public void onTaskCompleted(String s, QueueListModel queueListModel) {

    }

    @Override
    public void onValueSelected(Entry e,Highlight h) {
        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null)
            Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
        else
            Log.i("VAL SELECTED", "Value: " + entry.getY());
    }

    private int[] getColors() {

        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        /*for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }*/
        colors[1] = Color.parseColor("#DC143C");
        colors[0] = Color.parseColor("#008000");
        return colors;
    }


    @Override
    public void onNothingSelected() {

    }
}
