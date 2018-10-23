package ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;

/**
 * Created by Mark Khakimulin on 18.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class ConversionMarker extends MarkerView {


    private TextView tvContent;
    private Constants.ReportType type;
    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public ConversionMarker(Context context, int layoutResource,Constants.ReportType type) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);

        this.type = type;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        ConversionData data = (ConversionData) e.getData();

        if (type == Constants.ReportType.conversion) {
            tvContent.setText("Чеков: " + data.cheques + "\n" + "Посетителей: " + data.visitors);
        } else {
            tvContent.setText("Товаров: " + data.items + "\n" + "Чеков: " + data.cheques);
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
