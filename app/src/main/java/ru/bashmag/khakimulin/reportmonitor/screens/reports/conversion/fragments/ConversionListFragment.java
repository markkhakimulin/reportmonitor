package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.timessquare.CalendarCellDecorator;
import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseFragment;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.data.ConversionBarData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items.BarChartItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.charts.items.ChartItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.di.ConversionComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.list.ChartItemAdapter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.list.StoresAdapter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.mvp.ConversionPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

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
    SharedPreferences preferences;

    @Inject
    CompositeSubscription subscriptions;
    @Inject
    Resources resources;

    private StoresAdapter storesAdapter;
    private ChartItemAdapter<ChartItem> chartItemAdapter;
    private AlertDialog pickDateDialog;
    private DateFormat dateFormat = new SimpleDateFormat(FORMATDATE,Locale.getDefault());
    //private ConversionReportActivity context;
    private Date startDate,finishDate;
    private String startDateTitle,finishDateTitle;
    private ArrayList<String> chosenStoreList;
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


        setHasOptionsMenu(true);

        if (getArguments() != null) {
            type = (Constants.ReportType)getArguments().getSerializable(Constants.ReportType.class.getCanonicalName());
            startDatePref = type.toString()+Constants.PERIOD_STARTDATE;
            finishDatePref = type.toString()+Constants.PERIOD_FINISHDATE;
            periodTitlePref = type.toString()+Constants.PERIOD_TITLE_PREFERENCES;
            periodTypePref = type.toString()+Constants.PERIOD_TYPE_PREFERENCES;
            storesPref = type.toString()+Constants.CHOSEN_STORES;
        }


        com.github.mikephil.charting.utils.Utils.init(getActivity());

        chartItemAdapter = new ChartItemAdapter<>(getActivity());
        storesAdapter = new StoresAdapter(getActivity());

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

        long startDateInMills = preferences.getLong(startDatePref,0);
        long finishDateInMills = preferences.getLong(finishDatePref,0);

        if (startDateInMills >0 && finishDateInMills >0) {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.set(Calendar.AM_PM,Calendar.PM);

            calendar.setTimeInMillis(startDateInMills);
            startDate = calendar.getTime();

            calendar.setTimeInMillis(finishDateInMills);
            finishDate = calendar.getTime();
        }
        if (preferences.getStringSet(storesPref,null) != null) {
            chosenStoreList = new ArrayList<>(Objects.requireNonNull(preferences.getStringSet(storesPref, new HashSet<>())));
        } else {
            chosenStoreList = new ArrayList<>();
        }

        swipe.setOnRefreshListener(() -> {

            if (startDate == null || finishDate == null) {
                swipe.setRefreshing(false);
                ((ConversionReportActivity)getActivity()).onShowToast("Не выбран период");
                return;
            }

            if (chosenStoreList.size() == 0) {
                swipe.setRefreshing(false);
                ((ConversionReportActivity)getActivity()).onShowToast("Не выбрано ни одного магазина");
                return;
            }
            presenter.refresh(startDate,finishDate,chosenStoreList);
        });

        swipe.setColorSchemeColors(getColorWrapper(getActivity(),R.color.colorPrimary),
                getColorWrapper(getActivity(),R.color.colorAccent));


        list.setAdapter(chartItemAdapter);


        if (chartItemAdapter.getCount() > 0) {
            empty.setVisibility(GONE);
        }

        subscriptions.clear();
        subscriptions.add(respondToStoreFilterClick());
        subscriptions.add(respondToChartClick());

    }

    @Override
    protected boolean onInjectView() throws IllegalStateException {
        getComponent(ConversionComponent.class).inject(this);
        return true;
    }

    private Subscription respondToStoreFilterClick() {

        return itemStoreClicks().subscribe(storeId -> changeChosenStore(storeId));
    }

    private Subscription respondToChartClick() {

        return itemChartClicks().subscribe(data ->
                ((ConversionReportActivity)Objects.requireNonNull(getActivity()))
                        .addFragment(ConversionDailyFragment.newInstance(data,type),
                                true,
                                ConversionDailyFragment.TAG));
    }

    public void showCalendarInDialog() {

        Calendar lastYear = Calendar.getInstance(Locale.getDefault());
        lastYear.add(Calendar.YEAR, -2);

        Calendar today = Calendar.getInstance(Locale.getDefault());
        today.add(Calendar.DAY_OF_MONTH, 1);//потому что библиотека зачем то приводит к началу дня а потому убавляем минуту.Скорее всего это косяк либы

        CalendarPickerView dialogView = (CalendarPickerView) LayoutInflater.from(getActivity()).inflate(R.layout.pick_data_dialog, null, false);
        dialogView.setCustomDayView(new DefaultDayViewAdapter());
        dialogView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

                if (dialogView.getSelectedDates().size() > 1) {
                    startDate = dialogView.getSelectedDates().get(0);
                    startDateTitle = dateFormat.format(startDate);

                    Date todayDate = Calendar.getInstance(Locale.getDefault()).getTime();
                    Date lastDate = dialogView.getSelectedDates().get(dialogView.getSelectedDates().size()-1);

                    Calendar last = Calendar.getInstance(Locale.getDefault());
                    last.setTime(lastDate);

                    Calendar today = Calendar.getInstance(Locale.getDefault());
                    today.setTime(todayDate);

                    if (last.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                        lastDate = todayDate;
                    } else {
                        lastDate = Utils.getEndOfADay(lastDate);
                    }

                    finishDate = lastDate;
                    finishDateTitle = dateFormat.format(finishDate);

                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putInt(periodTypePref,R.id.action_custom_period_filter);
                    edit.putString(periodTitlePref,startDateTitle +"-"+finishDateTitle);
                    edit.putLong(startDatePref,startDate.getTime());
                    edit.putLong(finishDatePref,finishDate.getTime());
                    edit.apply();
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
        dialogView.setOnInvalidDateSelectedListener(new CalendarPickerView.OnInvalidDateSelectedListener() {
            @Override
            public void onInvalidDateSelected(Date date) {
                ((ConversionReportActivity)getActivity()).onShowToast("Некорректная дата "+date.toString());
            }
        });
        dialogView.setDecorators(Collections.<CalendarCellDecorator>emptyList());


        dialogView.init(Utils.getStartOfADay(lastYear.getTime()),today.getTime())//
                .inMode(CalendarPickerView.SelectionMode.RANGE); //

        if (startDate != null)
        dialogView.selectDate(startDate);
        if (finishDate != null)
        dialogView.selectDate(finishDate);

        pickDateDialog = new AlertDialog.Builder(getActivity()) //
                .setTitle("Выберите период отчета")
                .setView(dialogView)
                .setNeutralButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        pickDateDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                dialogView.fixDialogDimens();
            }
        });
        pickDateDialog.show();
    }

    public void showStores() {

        View view = LayoutInflater.from(getActivity()).inflate(android.R.layout.list_content, null, false);

        ListView listView = view.findViewById(android.R.id.list);
        listView.setAdapter(storesAdapter);
        AlertDialog dialog = new AlertDialog.Builder(getActivity()) //
                .setTitle("Выберите магазин")
                .setView(view)
                .setNeutralButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();

        dialog.show();
    }

    public void changeChosenStore(String chosenStoreId) {

        if (!chosenStoreList.contains(chosenStoreId)) {
            chosenStoreList.add(chosenStoreId);
        } else {
            chosenStoreList.remove(chosenStoreId);
        }

        Set<String> stores = new HashSet<>(chosenStoreList);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(storesPref,stores);
        editor.apply();
    }

    public void onShowStores(ArrayList<Store> storeItems) {

        storesAdapter.clear();
        storesAdapter.addAll(storeItems);

    }

    @Override
    public void setRefreshing(Boolean refreshing) {
        swipe.setRefreshing(refreshing);
    }

    public Observable<String> itemStoreClicks()
    {
        return storesAdapter.observeClicks();
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        int selected = preferences.getInt(periodTypePref,0);
        if (selected > 0) {
            SubMenu periodMenu = menu.findItem(R.id.action_period).getSubMenu();
            refreshSubMenu(periodMenu,selected);
        }
        super.onPrepareOptionsMenu(menu);
    }

    void refreshSubMenu(SubMenu subMenu,int selected) {
        for (int i = 0; i < subMenu.size(); i++) {

            if (subMenu.getItem(i).getItemId() != selected) {
                subMenu.getItem(i).setCheckable(false);
                subMenu.getItem(i).setTitle(getTitle(subMenu.getItem(i).getItemId()));
            } else {
                subMenu.getItem(i).setCheckable(true);
                subMenu.getItem(i).setChecked(true);
                String currentPeriodTitle = preferences.getString(periodTitlePref,"");
                subMenu.getItem(i).setTitle(getTitle(subMenu.getItem(i).getItemId())+" ("+currentPeriodTitle+")");
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.reports_base, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Calendar calendar;

        switch (item.getItemId()) {
            case R.id.action_stores_filter: {

                showStores();
                presenter.showStoreList(chosenStoreList);
                return true;
            }
            case R.id.action_custom_period_filter: {

                showCalendarInDialog();
                return true;
            }

            case R.id.action_period_month_filter: {

                calendar = Calendar.getInstance(Locale.getDefault());
                Date today = calendar.getTime();
                calendar.setTime(today);
                calendar.set(Calendar.DAY_OF_MONTH, 1);

                startDate = Utils.getStartOfADay(calendar.getTime());
                startDateTitle = dateFormat.format(startDate);

                finishDate = today;
                finishDateTitle = dateFormat.format(finishDate);

                break;
            }
            case R.id.action_period_week_filter: {

                calendar = Calendar.getInstance(Locale.getDefault());
                Date today = calendar.getTime();
                calendar.setTime(today);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                startDate = Utils.getStartOfADay(calendar.getTime());
                startDateTitle = dateFormat.format(startDate);

                finishDate = today;
                finishDateTitle = dateFormat.format(finishDate);


                break;
            }
            case R.id.action_period_today_filter: {

                calendar = Calendar.getInstance(Locale.getDefault());
                Date today = calendar.getTime();

                startDate = Utils.getStartOfADay(today);
                startDateTitle = dateFormat.format(startDate);

                finishDate = today;
                finishDateTitle = dateFormat.format(finishDate);

                break;
            }

            default: {
                int selected = preferences.getInt(periodTypePref,0);
                refreshSubMenu( item.getSubMenu(),selected);
                return true;
            }
        }

        String currentPeriodTitle = startDateTitle +"-"+finishDateTitle;

        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(periodTypePref,item.getItemId());
        edit.putString(periodTitlePref,currentPeriodTitle);
        edit.putLong(startDatePref,startDate.getTime());
        edit.putLong(finishDatePref,finishDate.getTime());
        edit.apply();

        return true;
    }

    public String getTitle(int id) {
        switch (id) {

            case R.id.action_stores_filter: {

                return getString(R.string.action_reports_stores_filter);
            }
            case R.id.action_custom_period_filter: {

                return getString(R.string.action_custom_period_filter);
            }
            case R.id.action_period_month_filter: {

                return getString(R.string.action_period_month_filter);
            }
            case R.id.action_period_week_filter: {

                return getString(R.string.action_period_week_filter);
            }
            case R.id.action_period_today_filter: {

                return getString(R.string.action_period_today_filter);
            }
        }
        return "";
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
