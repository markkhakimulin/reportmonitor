package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.list;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items.BarChartItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items.ChartItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import rx.Observable;
import rx.subjects.PublishSubject;

public class ChartItemAdapter<T extends ChartItem> extends ArrayAdapter<T>{

    private final PublishSubject<ConversionData> itemClicks = PublishSubject.create();

    public ChartItemAdapter(Context context) {
        super(context, 0);
    }

    public Observable<ConversionData> observeClicks() {
        return itemClicks;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = getItem(position).getView(position, convertView, getContext());
        ((BarChartItem)getItem(position)).setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                itemClicks.onNext((ConversionData)e.getData());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        // return the views type
        return getItem(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    public void resetItems() {
        for (int i = 0; i <getCount();i++) {
            BarChartItem chartItem = (BarChartItem)getItem(i);
            chartItem.getData().setValueTextSize(getContext().getResources().getDimension(R.dimen.default_data_set_text_size));
        }
    }

}