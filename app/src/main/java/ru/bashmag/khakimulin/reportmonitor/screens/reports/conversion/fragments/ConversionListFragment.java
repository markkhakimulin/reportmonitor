package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseFragment;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.data.ConversionBarData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items.BarChartItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items.ChartItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.ConversionComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.list.ChartItemAdapter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp.ConversionPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;

import static android.view.View.GONE;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE;


public class ConversionListFragment extends BaseFragment implements ConversionView {

    public static String TAG = ConversionListFragment.class.getCanonicalName();

    @BindView(R.id.chartList)
    ListView list;

    @BindView(R.id.swipe)
    public SwipeRefreshLayout swipe;

    @BindView(R.id.empty)
    public TextView empty;

    @Inject
    ConversionPresenter presenter;

    @Inject
    CompositeDisposable subscriptions;
    @Inject
    Resources resources;

    private ChartItemAdapter<ChartItem> chartItemAdapter;
    private DateFormat dateFormat = new SimpleDateFormat(FORMATDATE,Locale.getDefault());
    private Unbinder unbinder;
    private Constants.ReportType type;
    private Parcelable listState;

    public static ConversionListFragment newInstance(Constants.ReportType type) {

        ConversionListFragment fragment = new ConversionListFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.ReportType.class.getCanonicalName(),type);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.github.mikephil.charting.utils.Utils.init(getActivity());
        chartItemAdapter = new ChartItemAdapter<>(getActivity());
        if (getArguments() != null) {
            type = (Constants.ReportType)getArguments().getSerializable(Constants.ReportType.class.getCanonicalName());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void onViewInjected(Bundle savedInstanceState) {
        super.onViewInjected(savedInstanceState);

        swipe.setOnRefreshListener(() -> {
            presenter.refresh();
        });

        swipe.setColorSchemeColors(getColorWrapper(getActivity(),R.color.colorPrimary),
                getColorWrapper(getActivity(),R.color.colorAccent));


        list.setAdapter(chartItemAdapter);


        if (chartItemAdapter.getCount() > 0) {
            empty.setVisibility(GONE);
        }

        subscriptions.add(respondToChartClick());

        setRefreshing(true);
        presenter.refresh();

    }

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(ConversionComponent.class).inject(this);
        return true;
    }

    private Disposable respondToChartClick() {
        return itemChartClicks().subscribe(data -> {
            presenter.setConversionData(data);
            presenter.setStartDate(data.date);
            presenter.setFinishDate(Utils.getEndOfADay(data.date));
            ((ConversionReportActivity) Objects.requireNonNull(ConversionListFragment.this.getActivity()))
                    .addFragment(ConversionDailyFragment.newInstance(data,type), true, ConversionDailyFragment.TAG);
            ((ConversionReportActivity) Objects.requireNonNull(ConversionListFragment.this.getActivity()))
                    .invalidate();
        });
    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        swipe.setRefreshing(refreshing);
    }

    public Observable<ConversionData> itemChartClicks()
    {
        return chartItemAdapter.observeClicks();
    }

    public void onShowReport(ArrayList<ConversionData> data) {

        empty.setVisibility(GONE);

        ArrayList<ChartItem> chartItems = new ArrayList<>();

        HashMap<String,ArrayList<ConversionData>> stores = new HashMap<>();

        //parse data from server
        for (ConversionData conversionData:data) {

            if (!stores.containsKey(conversionData.storeTitle)) {
                stores.put(conversionData.storeTitle,new ArrayList<>());
            }

            stores.get(conversionData.storeTitle).add(conversionData);
        }

        //make charts
        for (String storename:stores.keySet()) {

            BarChartItem item = new BarChartItem(getActivity(),generateBarData(storename,stores.get(storename)));
            chartItems.add(item);
        }
        chartItemAdapter.clear();
        chartItemAdapter.addAll(chartItems);

    }

    @Override
    public String tag() {
        return TAG;
    }

    private BarData generateBarData(String name, ArrayList<ConversionData> conversionData) {


        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        String minDate = "";
        String maxDate = "";

        HashMap<Float,String> dateRanges = new HashMap<>();

        for (int i = 0; i < conversionData.size(); i++) {
            ConversionData data = conversionData.get(i);

            if (type == Constants.ReportType.conversion) {
                entries.add(new BarEntry(i, data.visitors == 0 ? 0f : (float) ((double) data.cheques / (double) data.visitors * 100), data));
            } else if (type == Constants.ReportType.fullness) {
                entries.add(new BarEntry(i, data.visitors == 0 ? 0f : (float) ((double) data.items / (double) data.cheques * 100), data));
            }

            if (i == 0) {
                minDate = dateFormat.format(data.date);
            }
            if (i == conversionData.size() -1) {
                maxDate = dateFormat.format(data.date);
            }

            dateRanges.put((float)i,dateFormat.format(data.date));

        }
        BarDataSet d = new BarDataSet(entries, String.format(Locale.getDefault(),"%s ( %s - %s)",name,minDate,maxDate));
        d.setColor(ColorTemplate.VORDIPLOM_COLORS[new Random().nextInt(ColorTemplate.VORDIPLOM_COLORS.length)]);
        d.setValueTextSize(resources.getDimension(R.dimen.default_data_set_text_size));

        ConversionBarData cd = new ConversionBarData(d,dateRanges);
        cd.setBarWidth(0.9f);
        return cd;
    }


    /**
     * Called when device is being rotate or became invisible
     **/
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        listState = list.onSaveInstanceState();
    }

    /**
     * Called when device has been rotated or became visible
     **/
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        chartItemAdapter.resetItems();
        if (listState!= null) {
            list.onRestoreInstanceState(listState);
        }

    }


}
