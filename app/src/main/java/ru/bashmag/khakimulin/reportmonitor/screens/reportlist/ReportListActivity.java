package ru.bashmag.khakimulin.reportmonitor.screens.reportlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.bashmag.khakimulin.reportmonitor.App;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.di.DaggerReportListComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.di.ReportListModule;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list.ReportAdapter;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list.ReportItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.mvp.ReportListPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import rx.Observable;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class ReportListActivity extends BaseActivity {

    @Inject
    ReportListPresenter presenter;

    @BindView(R.id.reportListView)
    RecyclerView list;


    ReportAdapter adapter;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ReportAdapter(this);

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
        DaggerReportListComponent.builder().appComponent(App.getAppComponent())
                .reportListModule(new ReportListModule(this)).build().inject(this);
    }

    public void setDataList(ArrayList<ReportItem> reports) {
        adapter.replaceAll(reports);
    }

    public Observable<Integer> itemClicks()
    {
        return adapter.observeClicks();
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_report_list;
    }

    public void goToReportActivity(Class reportClass, Constants.ReportType type) {

        Intent in = new Intent(this, reportClass);
        in.putExtra(Constants.ReportType.class.getCanonicalName(),type);
        startActivity(in);

    }

}