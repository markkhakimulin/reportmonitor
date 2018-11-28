package ru.bashmag.khakimulin.reportmonitor.screens.reports.dialog;

import com.squareup.timessquare.CalendarPickerView;

/**
 * Created by Mark Khakimulin on 08.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public interface PeriodPickerView {

    void setOnInvalidDateSelectedListener(CalendarPickerView.OnInvalidDateSelectedListener listener);
    void setOnDatesSelectedListener(OnDatesSelectedListener listener);
}
