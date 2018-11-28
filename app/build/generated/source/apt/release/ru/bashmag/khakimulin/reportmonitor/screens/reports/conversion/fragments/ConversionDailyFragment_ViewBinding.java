// Generated code from Butter Knife. Do not modify!
package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items.BarChartDaily;

public class ConversionDailyFragment_ViewBinding implements Unbinder {
  private ConversionDailyFragment target;

  @UiThread
  public ConversionDailyFragment_ViewBinding(ConversionDailyFragment target, View source) {
    this.target = target;

    target.chart = Utils.findOptionalViewAsType(source, R.id.chart, "field 'chart'", BarChartDaily.class);
    target.swipe = Utils.findRequiredViewAsType(source, R.id.swipe, "field 'swipe'", SwipeRefreshLayout.class);
    target.empty = Utils.findRequiredViewAsType(source, R.id.empty, "field 'empty'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ConversionDailyFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.chart = null;
    target.swipe = null;
    target.empty = null;
  }
}
