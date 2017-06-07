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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.realizer.schoolgenie.managment.exceptionhandler.ExceptionHandler;
import com.realizer.schoolgenie.managment.utils.Config;
import com.realizer.schoolgenie.managment.utils.FragmentBackPressedListener;
import com.realizer.schoolgenie.managment.utils.MyAxisLableFormatter;
import com.realizer.schoolgenie.managment.utils.MyAxisTeacherValueFormatter;
import com.realizer.schoolgenie.managment.utils.MyAxisValueFormatter;
import com.realizer.schoolgenie.managment.utils.MyValueFormatter;
import com.realizer.schoolgenie.managment.utils.OnTaskCompleted;
import com.realizer.schoolgenie.managment.utils.QueueListModel;

import java.util.ArrayList;

/**
 * Created by Win on 01-06-2017.
 */

public class TeacherActivityReportFragment extends Fragment implements OnTaskCompleted, FragmentBackPressedListener, OnChartValueSelectedListener {

    private BarChart mChart;
    ArrayList<String> mLabels;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
        View rootView = inflater.inflate(R.layout.student_attendance_report, container, false);

        ((DashboardActivity) getActivity()).getSupportActionBar().setTitle(Config.actionBarTitle("Teacher Activity", getActivity()));
        ((DashboardActivity) getActivity()).getSupportActionBar().show();

        mChart = (BarChart) rootView.findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        setChart();
        return rootView;
    }

    public void setChart(){
        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);

        float groupSpace = 0.10f;
        float barSpace = 0.02f; // x2 DataSet
        float barWidth = 0.46f; // x2 DataSet

        mLabels = new ArrayList<>();
        mLabels.add("Suvarna");
        mLabels.add("Abhijit");
        mLabels.add("Dhananjay");

        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaxValue(100f);
        leftAxis.setLabelCount(10);
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setGranularity(1f);
        xLabels.setGranularityEnabled(true);
        xLabels.setCenterAxisLabels(true);
        xLabels.setDrawGridLines(false);
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setValueFormatter(new IndexAxisValueFormatter(mLabels));

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(10f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();

        yVals1.add(new BarEntry(0,30f));
        yVals1.add(new BarEntry(1,35f));
        yVals1.add(new BarEntry(2,70f));

        yVals2.add(new BarEntry(0,45f));
        yVals2.add(new BarEntry(1,50f));
        yVals2.add(new BarEntry(2,55f));

        BarDataSet set1,set2;
        set1 = new BarDataSet(yVals1, "Homework");
        set2 = new BarDataSet(yVals2, "Classwork");

        set1.setColors(Color.parseColor("#3498DB"));
        set2.setColors(Color.parseColor("#FFC300"));

        BarData data = new BarData(set1,set2);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        mChart.getBarData().setBarWidth(barWidth);
        mChart.getXAxis().setAxisMinimum(0);
        mChart.getXAxis().setAxisMaximum(0 + mChart.getBarData().getGroupWidth(groupSpace, barSpace) * mLabels.size());
        mChart.groupBars(0, groupSpace, barSpace);
        mChart.invalidate();
    }

    @Override
    public void onFragmentBackPressed() {
        Intent i = new Intent(getActivity(),DashboardActivity.class);
        startActivity(i);
    }

    @Override
    public void onTaskCompleted(String s, QueueListModel queueListModel) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        BarEntry entry = (BarEntry) e;

        if (entry.getYVals() != null)
            Log.i("VAL SELECTED", "Value: " + entry.getYVals()[h.getStackIndex()]);
        else
            Log.i("VAL SELECTED", "Value: " + entry.getY());
    }

    @Override
    public void onNothingSelected() {

    }
}
