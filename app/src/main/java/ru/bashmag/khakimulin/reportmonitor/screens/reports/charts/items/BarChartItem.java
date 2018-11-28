package ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.formatters.StringValuesFormatter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.data.ConversionBarData;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class BarChartItem extends ChartItem{

    private OnChartValueSelectedListener mSelectionListener;
    private  Highlight highlight ;
    private  Entry entry ;
    private Context context;
    private Boolean longClick = false;


    public BarChartItem(Context context,ChartData<?> cd) {
        super(cd);
        this.context = context;
    }


    /**
     * Called when event onChartGestureEnd in OnChartGestureListener listener
     **/
    public void setOnChartValueSelectedListener(OnChartValueSelectedListener l) {
        this.mSelectionListener = l;
    }
    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, Context c) {

        final ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_barchart, null);
            holder.chart = convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);

        IAxisValueFormatter xAxisFormatter = new StringValuesFormatter(((ConversionBarData)mChartData).getDateRanges());

        IAxisValueFormatter percentFormatter = new PercentFormatter();

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(90);
        xAxis.setLabelCount(mChartData.getEntryCount());

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(percentFormatter);

        YAxis rightAxis = holder.chart.getAxisRight();

        rightAxis.setEnabled(false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setValueFormatter(percentFormatter);
        // set data
        holder.chart.setData((BarData) mChartData);

        // do not forget to refresh the chart
        holder.chart.fitScreen();
        holder.chart.setScaleYEnabled(false);
        holder.chart.postInvalidate();
        holder.chart.animateY(700);


        holder.chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                longClick = false;
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                if (entry != null && highlight != null && longClick) {

                    mSelectionListener.onValueSelected(entry, highlight);
                }
                longClick = false;
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

                if (entry != null && highlight != null) {
                    longClick = true;
                }
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
                longClick = false;
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                longClick = false;
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                longClick = false;
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                longClick = false;
                BarDataSet dataSet = (BarDataSet)((ConversionBarData) getData()).getDataSets().get(0);
                if (scaleX > 1) {
                    dataSet.setValueTextSize(context.getResources().getDimension(R.dimen.small_text_size));
                } else {
                    dataSet.setValueTextSize(context.getResources().getDimension(R.dimen.default_data_set_text_size));
                }
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                if (me.getAction() == MotionEvent.ACTION_SCROLL || me.getAction() == MotionEvent.ACTION_MOVE ) {
                    holder.chart.highlightValue(null);
                    entry = null;
                    highlight = null;
                }

            }
        });

        holder.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                entry = e;
                highlight = h;
                longClick = false;
            }

            @Override
            public void onNothingSelected() {
                entry = null;
                highlight = null;
                longClick = false;
            }
        });

        return convertView;
    }
    private static class ViewHolder {
        BarChart chart;
    }



}