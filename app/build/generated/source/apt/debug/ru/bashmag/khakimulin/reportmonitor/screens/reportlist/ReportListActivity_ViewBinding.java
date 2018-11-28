// Generated code from Butter Knife. Do not modify!
package ru.bashmag.khakimulin.reportmonitor.screens.reportlist;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import ru.bashmag.khakimulin.reportmonitor.R;

public class ReportListActivity_ViewBinding implements Unbinder {
  private ReportListActivity target;

  @UiThread
  public ReportListActivity_ViewBinding(ReportListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ReportListActivity_ViewBinding(ReportListActivity target, View source) {
    this.target = target;

    target.list = Utils.findRequiredViewAsType(source, R.id.reportListView, "field 'list'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReportListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.list = null;
  }
}
