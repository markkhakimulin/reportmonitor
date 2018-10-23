package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ru.bashmag.khakimulin.reportmonitor.App;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.di.DaggerReportListComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.di.ReportListModule;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.mvp.ReportListPresenter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionView;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.di.DaggerTurnoverComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.di.TurnoverModule;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.list.TurnoverAdapter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp.TurnoverPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import rx.Observable;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverReportActivity extends BaseActivity {

    @Inject
    TurnoverPresenter presenter;

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.swipe)
    public SwipeRefreshLayout swipe;

    //@BindView(R.id.empty)
    public TextView empty;

    private TurnoverAdapter adapter;
    private Date startDate;
    private Date finishDate;
    private List<String> chosenStoreList;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipe.setOnRefreshListener(() -> {

           /* if (startDate == null || finishDate == null) {
                swipe.setRefreshing(false);
                onShowToast("Не выбран период");
                return;
            }

            if (chosenStoreList.size() == 0) {
                swipe.setRefreshing(false);
                onShowToast("Не выбрано ни одного магазина");
                return;
            }*/
            presenter.refresh(startDate,finishDate,chosenStoreList);
        });

        swipe.setColorSchemeColors(getColorWrapper(this,R.color.colorPrimary),
                getColorWrapper(this,R.color.colorAccent));

        adapter = new TurnoverAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(manager);
        list.setAdapter(adapter);


        presenter.onCreate();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    @Override
    protected void resolveDaggerDependency() {
        DaggerTurnoverComponent.builder().appComponent(App.getAppComponent())
                .turnoverModule(new TurnoverModule(this)).build().inject(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_turnover;
    }

    public void addFragment(ConversionView fragment, Boolean addToBackStack, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            super.addFragment(R.id.fragmentContainer, fragment, addToBackStack);
        }
    }


    public Observable<TurnoverData> monthPlanClicks()
    {
        return adapter.observeMonthPlanClicks();
    }
    public Observable<TurnoverData> monthFactClicks()
    {
        return adapter.observeMonthFactClicks();
    }
    public Observable<TurnoverData> dailyPlanClicks()
    {
        return adapter.observeDailyPlanClicks();
    }
    public Observable<TurnoverData> dailyFactClicks()
    {
        return adapter.observeDailyFactClicks();
    }
    public Observable<ConversionData> conversionClicks()
    {
        return adapter.observeConversionClicks();
    }
    public Observable<ConversionData> fullnessClicks()
    {
        return adapter.observeFullnessClicks();
    }

    public void setDataList(ArrayList<TurnoverReportData> list) {
        swipe.setRefreshing(false);
        adapter.replaceAll(list);
    }

    public void gotoConversion(ConversionData data) {
    }

    public void gotoFullness(ConversionData data) {
    }

    public void gotoDailyFact(TurnoverData data) {
    }

    public void gotoDailyPlan(TurnoverData data) {
    }

    public void gotoMonthlyFact(TurnoverData data) {
    }

    public void gotoMonthlyPlan(TurnoverData data) {
    }
}