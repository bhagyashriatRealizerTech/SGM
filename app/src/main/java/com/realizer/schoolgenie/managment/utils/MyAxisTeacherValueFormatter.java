package com.realizer.schoolgenie.managment.utils;

/**
 * Created by Win on 01-06-2017.
 */

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisTeacherValueFormatter implements IAxisValueFormatter
{


    protected String[] mTeacher;

    public  MyAxisTeacherValueFormatter(String[] teacher){
        this.mTeacher = teacher;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = (int) value;
        return mTeacher[index];
    }
}
