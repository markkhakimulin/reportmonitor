// Generated code from Butter Knife. Do not modify!
package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import ru.bashmag.khakimulin.reportmonitor.R;

public class ConversionListFragment_ViewBinding implements Unbinder {
  private ConversionListFragment target;

  @UiThread
  public ConversionListFragment_ViewBinding(ConversionListFragment target, View source) {
    this.target = target;

    target.list = Utils.findRequiredViewAsType(source, R.id.chartList, "field 'list'", ListView.class);
    target.swipe = Utils.findRequiredViewAsType(source, R.id.swipe, "field 'swipe'", SwipeRefreshLayout.class);
    target.empty = Utils.findRequiredViewAsType(source, R.id.empty, "field 'empty'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ConversionListFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.list = null;
    target.swipe = null;
    target.empty = null;
  }
}
