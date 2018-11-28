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

public class SalesAdapter$GroupViewHolder_ViewBinding implements Unbinder {
  private SalesAdapter.GroupViewHolder target;

  @UiThread
  public SalesAdapter$GroupViewHolder_ViewBinding(SalesAdapter.GroupViewHolder target,
      View source) {
    this.target = target;

    target.store = Utils.findRequiredViewAsType(source, R.id.store, "field 'store'", TextView.class);
    target.employee = Utils.findRequiredViewAsType(source, R.id.employee, "field 'employee'", TextView.class);
    target.position = Utils.findRequiredViewAsType(source, R.id.position, "field 'position'", TextView.class);
    target.group = Utils.findRequiredViewAsType(source, R.id.group, "field 'group'", TextView.class);
    target.plan = Utils.findRequiredViewAsType(source, R.id.plan, "field 'plan'", TextView.class);
    target.fact = Utils.findRequiredViewAsType(source, R.id.fact, "field 'fact'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SalesAdapter.GroupViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.store = null;
    target.employee = null;
    target.position = null;
    target.group = null;
    target.plan = null;
    target.fact = null;
  }
}
