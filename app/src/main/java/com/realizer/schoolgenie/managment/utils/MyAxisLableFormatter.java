package com.realizer.schoolgenie.managment.utils;

/**
 * Created by Win on 01-06-2017.
 */

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MyAxisLableFormatter implements IAxisValueFormatter
{

    ArrayList<String> mLabels;

    public MyAxisLableFormatter(ArrayList<String> labels) {
        mLabels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = (int) value;
        String text ="";
            text =  mLabels.get(index);
        return text;
    }
}
