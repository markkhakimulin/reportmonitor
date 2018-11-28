// Generated code from Butter Knife. Do not modify!
package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import ru.bashmag.khakimulin.reportmonitor.R;

public class TurnoverReportActivity_ViewBinding implements Unbinder {
  private TurnoverReportActivity target;

  @UiThread
  public TurnoverReportActivity_ViewBinding(TurnoverReportActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TurnoverReportActivity_ViewBinding(TurnoverReportActivity target, View source) {
    this.target = target;

    target.list = Utils.findRequiredViewAsType(source, R.id.list, "field 'list'", RecyclerView.class);
    target.swipe = Utils.findRequiredViewAsType(source, R.id.swipe, "field 'swipe'", SwipeRefreshLayout.class);
    target.empty = Utils.findRequiredViewAsType(source, R.id.empty, "field 'empty'", TextView.class);
    target.day = Utils.findRequiredViewAsType(source, R.id.day, "field 'day'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TurnoverReportActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.list = null;
    target.swipe = null;
    target.empty = null;
    target.day = null;
  }
}
