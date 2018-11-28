// Generated code from Butter Knife. Do not modify!
package ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import ru.bashmag.khakimulin.reportmonitor.R;

public class ReportAdapter$ReportViewHolder_ViewBinding implements Unbinder {
  private ReportAdapter.ReportViewHolder target;

  @UiThread
  public ReportAdapter$ReportViewHolder_ViewBinding(ReportAdapter.ReportViewHolder target,
      View source) {
    this.target = target;

    target.tvName = Utils.findRequiredViewAsType(source, R.id.tvName, "field 'tvName'", TextView.class);
    target.tvDesc = Utils.findRequiredViewAsType(source, R.id.tvDesc, "field 'tvDesc'", TextView.class);
    target.tvNew = Utils.findRequiredViewAsType(source, R.id.tvNew, "field 'tvNew'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReportAdapter.ReportViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvName = null;
    target.tvDesc = null;
    target.tvNew = null;
  }
}
