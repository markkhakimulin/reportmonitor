package ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.data;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;

import java.util.HashMap;

/**
 * Created by Mark Khakimulin on 16.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class ConversionBarData extends BarData {

    private HashMap<Float,String> dateRanges;

    public ConversionBarData(BarDataSet d,HashMap<Float, String> dateRanges) {
        super(d);
        this.dateRanges = dateRanges;
    }

    public HashMap<Float, String> getDateRanges() {
        return dateRanges;
    }

    public void setDateRanges(HashMap<Float, String> dateRanges) {
        this.dateRanges = dateRanges;
    }
}
