package ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.formatters;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.HashMap;

/**
 * Created by Mark Khakimulin on 04.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class StringValuesFormatter implements IAxisValueFormatter
{

    protected HashMap<Float,String> values;

    public StringValuesFormatter(HashMap<Float,String> values) {
        this.values = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return values.get(value);
    }
}