// Generated code from Butter Knife. Do not modify!
package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.list;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import ru.bashmag.khakimulin.reportmonitor.R;

public class SalesAdapter$ChildViewHolder_ViewBinding implements Unbinder {
  private SalesAdapter.ChildViewHolder target;

  @UiThread
  public SalesAdapter$ChildViewHolder_ViewBinding(SalesAdapter.ChildViewHolder target,
      View source) {
    this.target = target;

    target.group = Utils.findRequiredViewAsType(source, R.id.group, "field 'group'", TextView.class);
    target.plan = Utils.findRequiredViewAsType(source, R.id.plan, "field 'plan'", TextView.class);
    target.fact = Utils.findRequiredViewAsType(source, R.id.fact, "field 'fact'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SalesAdapter.ChildViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.group = null;
    target.plan = null;
    target.fact = null;
  }
}
