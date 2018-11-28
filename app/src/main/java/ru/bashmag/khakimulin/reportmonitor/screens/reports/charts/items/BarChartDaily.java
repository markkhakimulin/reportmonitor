package ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.BarHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.BarChartRenderer;

import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.data.ConversionBarData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.formatters.StringValuesFormatter;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class BarChartDaily extends BarChart{

    private Typeface mTf;

    public BarChartDaily(Context context) {
        super(context);
    }

    public BarChartDaily(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChartDaily(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void init(BarData data) {
        //super.init();

        // apply styling
        getDescription().setEnabled(false);
        setDrawGridBackground(false);
        setDrawBarShadow(false);
        setWillNotDraw(false);

        IAxisValueFormatter xAxisFormatter = new StringValuesFormatter(((ConversionBarData)data).getDateRanges());

        IAxisValueFormatter percentFormatter = new PercentFormatter();

        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTf);
        //xAxis.setDrawGridLines(false);
        //xAxis.setDrawAxisLine(true);
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setGranularity(1f);
        //xAxis.setCenterAxisLabels(true);
        xAxis.setLabelRotationAngle(90);
        xAxis.setLabelCount(data.getEntryCount());
        //xAxis.setGranularityEnabled(false);

        YAxis leftAxis = getAxisLeft();
        //leftAxis.setTypeface(mTf);
        //leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setValueFormatter(percentFormatter);

        YAxis rightAxis = getAxisRight();
        //rightAxis.setTypeface(mTf);
        //rightAxis.setLabelCount(5, false);
        rightAxis.setEnabled(false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setValueFormatter(percentFormatter);

        //mChartData.setValueTypeface(mTf);

        // set data
        setData(data);
        setDoubleTapToZoomEnabled(false);
        //holder.chart.setAutoScaleMinMaxEnabled(true);

        // do not forget to refresh the chart
        fitScreen();
        setAutoScaleMinMaxEnabled(false);
        invalidate();
        animateY(700);

        setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) { }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) { }

            @Override
            public void onChartLongPressed(MotionEvent me) {

                Highlight highlight =  getHighlightByTouchPoint(me.getX(),me.getY());
                BarEntry entry = (BarEntry) data.getEntryForHighlight(highlight);
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

                fitScreen();
                invalidate();
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) { }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) { }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) { }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) { }
        });

    }

}