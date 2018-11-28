package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments;

import android.content.res.Resources;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseFragment;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.data.ConversionBarData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items.BarChartDaily;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items.ConversionMarker;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.ConversionComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp.ConversionPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;

import static android.view.View.GONE;
import static ru.bashmag.khakimulin.reportmonitor.utils.Constants.FORMATDATE_HOUR;


public class ConversionDailyFragment extends BaseFragment implements ConversionView {

    public static String TAG = ConversionDailyFragment.class.getCanonicalName();

    @Nullable
    @BindView(R.id.chart)
    BarChartDaily chart;

    @BindView(R.id.swipe)
    public SwipeRefreshLayout swipe;

    @BindView(R.id.empty)
    public TextView empty;

    @Inject
    ConversionPresenter presenter;

    @Inject
    Resources resources;

    private DateFormat dateFormat = new SimpleDateFormat(FORMATDATE_HOUR,Locale.getDefault());
    private Date startDate,finishDate;
    private ArrayList<String> chosenStoreList = new ArrayList<>();
    private Unbinder unbinder;
    private ConversionData conversionData;
    private Constants.ReportType type;

    public static ConversionDailyFragment newInstance(ConversionData data,Constants.ReportType type) {
        ConversionDailyFragment fragment = new ConversionDailyFragment();
        Bundle args = new Bundle();
        args.putParcelable(ConversionData.class.getCanonicalName(),data);
        args.putSerializable(Constants.ReportType.class.getCanonicalName(),type);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            conversionData = getArguments().getParcelable(ConversionData.class.getCanonicalName());

            assert conversionData != null;

            startDate = Utils.getStartOfADay(conversionData.date);
            finishDate =  Utils.getEndOfADay(conversionData.date);

            chosenStoreList = new ArrayList<>();
            chosenStoreList.add(conversionData.storeId);

            type = (Constants.ReportType)getArguments().getSerializable(Constants.ReportType.class.getCanonicalName());
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion_daily, container, false);
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

        setRefreshing(true);

        swipe.setOnRefreshListener(() -> presenter.refreshDaily(startDate,finishDate,chosenStoreList));

        swipe.setColorSchemeColors(getColorWrapper(getActivity(),R.color.colorPrimary),
                getColorWrapper(getActivity(),R.color.colorAccent));

        Objects.requireNonNull(chart).setNoDataText("");

        presenter.refreshDaily(startDate,finishDate,chosenStoreList);

    }

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(ConversionComponent.class).inject(this);
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public void onShowStores(ArrayList<Store> stores) {

    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        swipe.setRefreshing(refreshing);
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    public void onShowReport(ArrayList<ConversionData> data) {

        empty.setVisibility(GONE);
        if (data.size() > 0) {

            chart.init(generateBarData(conversionData.storeTitle, data));
            chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    if (chart.getMarker() == null) {
                        ConversionMarker mv = new ConversionMarker(getActivity(), R.layout.text_marker, type);
                        mv.setChartView(chart); // For bounds control
                        chart.setMarker(mv); // Set the marker to the chart
                    }
                }

                @Override
                public void onNothingSelected() {
                    chart.setMarker(null);
                }
            });
        }
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
            } else {
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
        d.setValueTextSize(Math.max(resources.getDimension(R.dimen.normal_text_size) - dateRanges.size(),resources.getDimension(R.dimen.small_text_size)));

        ConversionBarData cd = new ConversionBarData(d,dateRanges);
        cd.setBarWidth(0.9f);
        return cd;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }


}
