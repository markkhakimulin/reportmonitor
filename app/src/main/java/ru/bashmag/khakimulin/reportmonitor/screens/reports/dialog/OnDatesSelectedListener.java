package ru.bashmag.khakimulin.reportmonitor.screens.reports.dialog;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Date;
import java.util.List;

/**
 * Created by Mark Khakimulin on 10.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public interface OnDatesSelectedListener extends CalendarPickerView.OnDateSelectedListener {
    void onDatesSelected(List<Date> dates);
}
