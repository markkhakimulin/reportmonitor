package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import ru.bashmag.khakimulin.reportmonitor.App;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseActivity;
import ru.bashmag.khakimulin.reportmonitor.core.di.HasComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.ConversionComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.ConversionModule;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.DaggerConversionComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionDailyFragment;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionListFragment;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionView;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp.ConversionPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */



public class ConversionReportActivity extends BaseActivity  implements HasComponent<ConversionComponent> {


    private ConversionComponent conversionComponent;
    @Inject
    public ConversionPresenter conversionPresenter;
    public ConversionView fragment;
    @Inject
    Resources rs;

    public Constants.ReportType type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getIntent().hasExtra(Constants.ReportType.class.getCanonicalName())) {

            type = (Constants.ReportType) getIntent().getSerializableExtra(Constants.ReportType.class.getCanonicalName());

            if (getIntent().getAction() != null && getIntent().getAction().contentEquals("ref")) {

                ConversionData conversionData = getIntent().getParcelableExtra(ConversionData.class.getCanonicalName());
                conversionPresenter.setConversionData(conversionData);
                addFragment(ConversionDailyFragment.newInstance(conversionData,type),false,ConversionDailyFragment.TAG);

            } else {
                addFragment(ConversionListFragment.newInstance(type),false,ConversionListFragment.TAG);
            }
        }
        conversionPresenter.onCreate();
        invalidate();
    }

    public void invalidate() {
        String stringBuilder = rs.getString(presenter.getTitle(type)) +
                presenter.generatePeriodTitle(new SimpleDateFormat(Constants.FORMATDATE, Locale.getDefault()));
        setTitle(stringBuilder);
    }

    public void addFragment(ConversionView fragment,Boolean addToBackStack,String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            this.fragment = fragment;
            super.addFragment(R.id.fragmentContainer, fragment, addToBackStack);
        }
    }
    public void replaceFragment(ConversionView fragment,Boolean addToBackStack,String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            this.fragment = fragment;
            super.replaceFragment(R.id.fragmentContainer, fragment, addToBackStack);
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
        if (fragment != null) {
            fragment.onDestroy();
        }

    }

    @Override
    protected void resolveDaggerDependency() {

        conversionComponent = DaggerConversionComponent.builder()
                .appComponent(App.getAppComponent())
                .conversionModule(new ConversionModule(this, startDate, finishDate, userId, chosenStoreList)).build();
        conversionComponent.inject(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reports, menu);
        return true;
    }

    @Override
    protected int getContentView() {

        return R.layout.activity_conversion_chart;
    }

    @Override
    public ConversionComponent getComponent() {
        return conversionComponent;
    }

    public void saveSharedPreference(int type, String title, long startDate, long finishDate, ArrayList<String> arrayList) { }


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
