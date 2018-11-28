// Generated code from Butter Knife. Do not modify!
package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.list;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import ru.bashmag.khakimulin.reportmonitor.R;

public class TurnoverAdapter$ReportViewHolder_ViewBinding implements Unbinder {
  private TurnoverAdapter.ReportViewHolder target;

  @UiThread
  public TurnoverAdapter$ReportViewHolder_ViewBinding(TurnoverAdapter.ReportViewHolder target,
      View source) {
    this.target = target;

    target.store = Utils.findRequiredViewAsType(source, R.id.store, "field 'store'", TextView.class);
    target.monthPlan = Utils.findRequiredViewAsType(source, R.id.month_plan, "field 'monthPlan'", TextView.class);
    target.monthFact = Utils.findRequiredViewAsType(source, R.id.month_fact, "field 'monthFact'", TextView.class);
    target.dailyPlan = Utils.findRequiredViewAsType(source, R.id.daily_plan, "field 'dailyPlan'", TextView.class);
    target.dailyFact = Utils.findRequiredViewAsType(source, R.id.daily_fact, "field 'dailyFact'", TextView.class);
    target.conversion = Utils.findRequiredViewAsType(source, R.id.conversion, "field 'conversion'", TextView.class);
    target.fullness = Utils.findRequiredViewAsType(source, R.id.fullness, "field 'fullness'", TextView.class);
    target.dailyPlanContainer = Utils.findRequiredViewAsType(source, R.id.daily_plan_container, "field 'dailyPlanContainer'", ViewGroup.class);
    target.dailyFactContainer = Utils.findRequiredViewAsType(source, R.id.daily_fact_container, "field 'dailyFactContainer'", ViewGroup.class);
    target.conversionContainer = Utils.findRequiredViewAsType(source, R.id.conversion_container, "field 'conversionContainer'", ViewGroup.class);
    target.fullnessContainer = Utils.findRequiredViewAsType(source, R.id.fullness_container, "field 'fullnessContainer'", ViewGroup.class);
    target.exchange = Utils.findRequiredViewAsType(source, R.id.exchange, "field 'exchange'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TurnoverAdapter.ReportViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.store = null;
    target.monthPlan = null;
    target.monthFact = null;
    target.dailyPlan = null;
    target.dailyFact = null;
    target.conversion = null;
    target.fullness = null;
    target.dailyPlanContainer = null;
    target.dailyFactContainer = null;
    target.conversionContainer = null;
    target.fullnessContainer = null;
    target.exchange = null;
  }
}
