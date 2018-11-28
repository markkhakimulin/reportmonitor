package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.bashmag.khakimulin.reportmonitor.App;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseActivity;
import ru.bashmag.khakimulin.reportmonitor.core.di.HasComponent;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.ConversionComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.ConversionModule;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.DaggerConversionComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionListFragment;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionView;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp.ConversionPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.rx.RxSchedulers;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */



public class ConversionReportActivity extends BaseActivity  implements HasComponent<ConversionComponent> {

    @Inject
    Context context;

    @Inject
    public ConversionPresenter presenter;

    @Inject
    SharedPreferences preferences;

    ConversionView fragment;

    private ConversionComponent conversionComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getIntent().hasExtra(Constants.ReportType.class.getCanonicalName())) {

            Constants.ReportType type = (Constants.ReportType) getIntent().getSerializableExtra(Constants.ReportType.class.getCanonicalName());

            addFragment(ConversionListFragment.newInstance(type),false,ConversionListFragment.TAG);

            if (type == Constants.ReportType.conversion) {
                setTitle(R.string.conversion_report_activity_name);
            } else {
                setTitle(R.string.fullness_report_activity_name);
            }
        }

    }

    public void addFragment(ConversionView fragment,Boolean addToBackStack,String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            this.fragment = fragment;
            super.addFragment(R.id.fragmentContainer, fragment, addToBackStack);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    protected void resolveDaggerDependency() {

        conversionComponent = DaggerConversionComponent.builder().appComponent(App.getAppComponent())
                .conversionModule(new ConversionModule(this)).build();
        conversionComponent.inject(this);

    }

    @Override
    protected int getContentView() {

        return R.layout.activity_conversion_chart;
    }

    @Override
    public ConversionComponent getComponent() {
        return conversionComponent;
    }

    public void onShowStores(ArrayList<Store> stores) {
        fragment.onShowStores(stores);
    }
    public void setRefreshing(Boolean refreshing) {
        fragment.setRefreshing(refreshing);
    }
    public void showMessage(String message) {
        showYesNoMessageDialog(message,null);
    }
    public void onShowReport(ArrayList<ConversionData> data) {
        fragment.onShowReport(data);
    }



}
