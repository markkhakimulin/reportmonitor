package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import ru.bashmag.khakimulin.reportmonitor.App;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.di.DaggerSalesComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.di.SalesModule;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.list.SalesAdapter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.sales.mvp.SalesPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import rx.Observable;

import static android.view.View.GONE;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

public class SalesReportActivity extends BaseActivity{

    @Inject
    public SalesPresenter salesPresenter;

    @Inject
    Resources rs;

    @BindView(R.id.list)
    ExpandableListView list;

    @BindView(R.id.swipe)
    public SwipeRefreshLayout swipe;

    @BindView(R.id.empty)
    public TextView empty;

    private SalesAdapter salesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        salesAdapter = new SalesAdapter(this);

        swipe.setOnRefreshListener(() -> {
            salesPresenter.refresh();
        });

        swipe.setColorSchemeColors(getColorWrapper(this,R.color.colorPrimary),
                getColorWrapper(this,R.color.colorAccent));


        swipe.setEnabled(false);
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (swipe == null || swipe.getChildCount() == 0) ? 0 : swipe.getChildAt(0).getTop();
                swipe.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        list.setAdapter(salesAdapter);


        if (!salesAdapter.isEmpty()) {
            empty.setVisibility(GONE);
        }

        salesPresenter.onCreate();

        invalidate();
    }

    @Override
    protected void invalidate() {
        setTitle(rs.getString(presenter.getTitle(Constants.ReportType.sales)) + presenter.generatePeriodTitle(new SimpleDateFormat(FORMATDATE,Locale.getDefault())));
    }

    @Override
    protected void onStart() {
        super.onStart();
        swipe.setRefreshing(true);
        salesPresenter.refresh();
    }


    @Override
    protected void resolveDaggerDependency() {

        DaggerSalesComponent.builder()
                .appComponent(App.getAppComponent())
                .salesModule(new SalesModule(this,startDate,finishDate,userId,chosenStoreList))
                .build().inject(this);
    }

    @Override
    protected int getContentView() {

        return R.layout.activity_sales;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reports, menu);
        return true;
    }

    public void setRefreshing(Boolean refreshing) {
        swipe.setRefreshing(refreshing);
    }
    public void showMessage(String message) {
        showYesNoMessageDialog(message,null);
    }

    public void onShowReport(ArrayList<SalesData> data) {
        empty.setVisibility(GONE);
        setRefreshing(false);
        salesAdapter.setData(data);
    }

    public Observable<Integer> groupClicks()
    {
        return salesAdapter.observeGroupsClicks();
    }

    public void requestLayout(int position) {
        //list.requestLayout();
        if (list.isGroupExpanded(position)) list.collapseGroup(position); else
        list.expandGroup(position);
        list.requestLayout();


    }


}
