package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import ru.bashmag.khakimulin.reportmonitor.App;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.SalesReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.di.DaggerTurnoverComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.di.TurnoverModule;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.list.TurnoverAdapter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.mvp.TurnoverPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import rx.Observable;

import static android.view.View.GONE;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.CHOSEN_STORES;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.PERIOD_FINISHDATE;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.PERIOD_STARTDATE;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.PERIOD_TITLE_PREFERENCES;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.PERIOD_TYPE_PREFERENCES;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverReportActivity extends BaseActivity {

    @Inject
    TurnoverPresenter turnoverPresenter;

    @Inject
    Resources rs;

    @BindView(R.id.list)
    RecyclerView list;

    @BindView(R.id.swipe)
    public SwipeRefreshLayout swipe;

    @BindView(R.id.empty)
    public TextView empty;

    @BindView(R.id.day)
    public TextView day;

    private TurnoverAdapter adapter;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipe.setOnRefreshListener(() -> {
            turnoverPresenter.refresh();
        });

        swipe.setColorSchemeColors(getColorWrapper(this,R.color.colorPrimary),
                getColorWrapper(this,R.color.colorAccent));

        adapter = new TurnoverAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        list.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, manager.getOrientation());
        list.addItemDecoration(dividerItemDecoration);
        list.setItemAnimator(new DefaultItemAnimator());
        list.setAdapter(adapter);

        presenter.onCreate();
        invalidate();
    }

    protected void  invalidate() {
        day.setText(String.format("День (%s)",dateFormat.format(presenter.getFinishDate())));

        setTitle(String.format("%s%s - %s",
                rs.getString(presenter.getTitle(Constants.ReportType.turnover)),
                presenter.generatePeriodTitle(new SimpleDateFormat(FORMATDATE,Locale.getDefault())),
                userTitle));
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


    public void saveSharedPreference(int type,String title,long startDate,long finishDate,ArrayList<String> chosenStoreList) {
        SharedPreferences.Editor edit = sp.edit();
        if (type != 0)
            edit.putInt(PERIOD_TYPE_PREFERENCES,type);
        if (!TextUtils.isEmpty(title))
            edit.putString(PERIOD_TITLE_PREFERENCES,title);
        if (startDate != 0)
            edit.putLong(PERIOD_STARTDATE,startDate);
        if (finishDate != 0)
            edit.putLong(PERIOD_FINISHDATE,finishDate);
        if (chosenStoreList != null) {
            Set<String> stores = new HashSet<>(chosenStoreList);
            edit.putStringSet(CHOSEN_STORES,stores);
        }
        edit.apply();

        invalidate();
    }
    public void updateStatusList(ArrayList<TurnoverReportData> list) {
        adapter.replaceAll(list);
    }

    public Observable<String> storeClicks()
    {
        return adapter.observeStoresClicks();
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

        empty.setVisibility(GONE);
        swipe.setRefreshing(false);
        adapter.replaceAll(list);
    }

    public void setRefreshing(Boolean refreshing) {
        swipe.setRefreshing(refreshing);
    }

    public void gotoConversion(ConversionData data) {

        if (data.isEmpty()) return;

        ArrayList<String> hashSet = new ArrayList<>();
        hashSet.add(data.storeId);
        Intent in = new Intent(this, ConversionReportActivity.class);
        in.setAction("ref");
        in.putExtra(Constants.ReportType.class.getCanonicalName(),Constants.ReportType.conversion);
        in.putExtra(Constants.PERIOD_STARTDATE,Utils.getStartOfADay(presenter.getFinishDate()).getTime());
        in.putExtra(Constants.PERIOD_FINISHDATE,presenter.getFinishDate().getTime());
        in.putExtra(ConversionData.class.getCanonicalName(),data);
        in.putExtra(Constants.USER_ID,presenter.getUserId());
        in.putExtra(Constants.USER_TITLE,presenter.getUserTitle());
        in.putExtra(Constants.CHOSEN_STORES,hashSet);
        startActivity(in);
    }

    public void gotoFullness(ConversionData data) {

        if (data.isEmpty()) return;

        ArrayList<String> hashSet = new ArrayList<>();
        hashSet.add(data.storeId);
        Intent in = new Intent(this, ConversionReportActivity.class);
        in.setAction("ref");
        in.putExtra(Constants.ReportType.class.getCanonicalName(),Constants.ReportType.fullness);
        in.putExtra(Constants.PERIOD_STARTDATE,Utils.getStartOfADay(presenter.getFinishDate()).getTime());
        in.putExtra(Constants.PERIOD_FINISHDATE,presenter.getFinishDate().getTime());
        in.putExtra(ConversionData.class.getCanonicalName(),data);
        in.putExtra(Constants.CHOSEN_STORES,hashSet);
        in.putExtra(Constants.USER_ID,presenter.getUserId());
        in.putExtra(Constants.USER_TITLE,presenter.getUserTitle());
        startActivity(in);

    }

    public void gotoDailyFact(TurnoverData data) {

        if (data.isEmpty()) return;

        ArrayList<String> hashSet = new ArrayList<>();
        hashSet.add(data.storeId);
        Intent in = new Intent(this, SalesReportActivity.class);
        in.setAction("ref");
        in.putExtra(Constants.PERIOD_STARTDATE,Utils.getStartOfADay(presenter.getFinishDate()).getTime());
        in.putExtra(Constants.PERIOD_FINISHDATE,presenter.getFinishDate().getTime());
        in.putExtra(Constants.USER_ID,presenter.getUserId());
        in.putExtra(Constants.USER_TITLE,presenter.getUserTitle());
        in.putExtra(Constants.CHOSEN_STORES,hashSet);
        startActivity(in);
    }

    public void gotoDailyPlan(TurnoverData data) {


        if (data.isEmpty()) return;

        ArrayList<String> hashSet = new ArrayList<>();
        hashSet.add(data.storeId);

        Intent in = new Intent(this, SalesReportActivity.class);
        in.setAction("ref");
        in.putExtra(Constants.PERIOD_STARTDATE,Utils.getStartOfADay(presenter.getFinishDate()).getTime());
        in.putExtra(Constants.PERIOD_FINISHDATE,presenter.getFinishDate().getTime());
        in.putExtra(Constants.USER_ID,presenter.getUserId());
        in.putExtra(Constants.USER_TITLE,presenter.getUserTitle());
        in.putExtra(Constants.CHOSEN_STORES,hashSet);
        startActivity(in);
    }


}