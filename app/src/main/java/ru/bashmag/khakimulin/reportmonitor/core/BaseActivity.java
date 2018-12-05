package ru.bashmag.khakimulin.reportmonitor.core;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.DefaultDayViewAdapter;

import org.reactivestreams.Subscription;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.DisposableSubscriber;
import ru.bashmag.khakimulin.reportmonitor.BuildConfig;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.login.LoginActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionView;
import ru.bashmag.khakimulin.reportmonitor.core.list.StoresAdapter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public abstract class BaseActivity extends AppCompatActivity
        implements CalendarPickerView.OnDateSelectedListener,
                    CalendarPickerView.OnInvalidDateSelectedListener,
                   DialogInterface.OnShowListener{

    @Inject
    public BasePresenter presenter;
    @Inject
    public SharedPreferences sp;
    @Inject
    protected CompositeDisposable subscriptions;

    protected Unbinder binder;
    protected ArrayList<String> chosenStoreList;
    protected DateFormat dateFormat = new SimpleDateFormat(Constants.FORMATDATE, Locale.getDefault());
    protected Date finishDate,startDate,previousDate = null;
    protected ProgressDialog mProgressDialog;
    protected StoresAdapter storesAdapter;
    protected String userId;
    protected String userTitle;
    protected CalendarPickerView dialogView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startDate = new Date(getIntent().getLongExtra(Constants.PERIOD_STARTDATE,0));
        finishDate = new Date(getIntent().getLongExtra(Constants.PERIOD_FINISHDATE,0));
        chosenStoreList = getIntent().getStringArrayListExtra(Constants.CHOSEN_STORES);
        if (getIntent().hasExtra(Constants.USER_ID)) {
            userId = getIntent().getStringExtra(Constants.USER_ID);
        }
        if (getIntent().hasExtra(Constants.USER_TITLE)) {
            userTitle = getIntent().getStringExtra(Constants.USER_TITLE);
        }

        resolveDaggerDependency();
        setupWindowAnimations();
        setContentView(getContentView());
        binder = ButterKnife.bind(this);

        storesAdapter = new StoresAdapter(this);

        subscriptions.add(respondToStoreFilterClick());
    }

    protected abstract void resolveDaggerDependency();
    protected abstract int getContentView();

    protected void showBackArrow() {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
    }

    public void showYesNoMessageDialog(String title, String message,@Nullable final Callable<Void> positiveCallback,@Nullable final Callable negativeCallback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(title);
        if (positiveCallback != null)
            builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
                try {
                    dialog.dismiss();
                    positiveCallback.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        if (negativeCallback != null)
            builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
                try {
                    dialog.dismiss();
                    negativeCallback.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        builder.setNeutralButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        binder.unbind();
    }

    public void showYesNoMessageDialog(String message,@Nullable final Callable<Void> positiveCallback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            try {
                dialog.dismiss();

                if (positiveCallback != null) {
                    positiveCallback.call();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        builder.create().show();
    }

    public void showProgressDialog(String title,int progress,int max) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
            if (max > 0) {
                mProgressDialog.setProgress(progress);
                mProgressDialog.setMax(max);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            } else {
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            }
        }
        if (progress > 0) {
            mProgressDialog.setProgress(progress);
        }
        mProgressDialog.setMessage(title);

        mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void onShowToast(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }


    public int getColorWrapper(Context context, int id) {
        return Utils.getColorWrapper(context,id);
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment The fragment to be added.
     */
    public void addFragment(int containerViewId, ConversionView fragment, Boolean addToBackStack) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, (Fragment) fragment,fragment.tag());
        if (addToBackStack)
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void replaceFragment(int containerViewId, ConversionView fragment, Boolean addToBackStack) {

        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, (Fragment) fragment,fragment.tag());
        if (addToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void changeChosenStore(String chosenStoreId) {
        presenter.setChosenStore(chosenStoreId);
    }

    public String getTitle(int id) {
        if (id == R.id.action_about) {
            return getString(R.string.action_info_about);
        }
        if (id == R.id.action_custom_period_filter) {
            return getString(R.string.action_custom_period_filter);
        }
        if (id == R.id.action_version) {
            return getString(R.string.action_info_version);
        }
        switch (id) {
            case R.id.action_period_month_filter /*2131296283*/:
                return getString(R.string.action_period_month_filter);
            case R.id.action_period_today_filter /*2131296284*/:
                return getString(R.string.action_period_today_filter);
            case R.id.action_period_week_filter /*2131296285*/:
                return getString(R.string.action_period_week_filter);
            case R.id.action_stores_filter /*2131296286*/:
                return getString(R.string.action_reports_stores_filter);
            default:
                return "";
        }
    }

    public void gotoNETSettings() {
        startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
    }

    @TargetApi(24)
    public void gotoVPNSettings() {
        startActivity(new Intent("android.settings.VPN_SETTINGS"));
    }

    protected abstract void invalidate();

    public Observable<String> itemStoreClicks() {
        return storesAdapter.observeClicks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reports_base, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {

        Calendar calendar;
        int itemId = menuItem.getItemId();
        switch (itemId) {
            case R.id.action_period_month_filter: {

                calendar = Calendar.getInstance(Locale.getDefault());
                Date today = calendar.getTime();
                calendar.set(Calendar.DAY_OF_MONTH, 1);

                presenter.setStartDate(Utils.getStartOfADay(calendar.getTime()));
                presenter.setFinishDate(today);
                break;
            }
             case R.id.action_period_week_filter: {
                 calendar = Calendar.getInstance(Locale.getDefault());
                 Date today = calendar.getTime();
                 calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                 presenter.setStartDate(Utils.getStartOfADay(calendar.getTime()));
                 presenter.setFinishDate(today);
                break;
            }
             case R.id.action_period_today_filter: {
                 calendar = Calendar.getInstance(Locale.getDefault());
                 Date today = calendar.getTime();

                 presenter.setStartDate(Utils.getStartOfADay(today));
                 presenter.setFinishDate(today);
                break;
            }
            case R.id.action_custom_period_filter: {
                showCalendarInDialog();
                break;
            }
             case R.id.action_version: {

                 String version = "x.x.x";
                 try {
                     PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),0);
                     version = info.versionName;
                 } catch (PackageManager.NameNotFoundException ignored) {}

                 String debuggable = "";
                 try {
                     if (0 != (getApplicationInfo().flags &
                             ApplicationInfo.FLAG_DEBUGGABLE)) {
                         debuggable = " debug";
                     }
                 } catch (Exception ignored){}
                 version = version.concat(debuggable);
                 AlertDialog.Builder builder = new AlertDialog.Builder(this);
                 builder.setMessage(version);
                 builder.setTitle(R.string.action_info_version);
                 builder.create().show();
                 return true;
            }
             case R.id.exit: {
                 SharedPreferences.Editor edit = sp.edit();
                 edit.putString(Constants.USER_ID_PREFERENCES, Constants.EMPTY_ID);
                 edit.putString(Constants.USER_TITLE_PREFERENCES, "");
                 edit.apply();

                 Intent intent = new Intent(this, LoginActivity.class);
                 intent.putExtra(Constants.USER_ID, presenter.getUserId());
                 startActivity(intent);
                 finish();
                return true;
            }
             case R.id.action_stores_filter: {
                 showStores();
                 presenter.showStoreList();
                 return true;
            }
            case R.id.action_about: {
                showAbout();
                return true;
            }
            default: {
                int selected = sp.getInt(Constants.PERIOD_TYPE_PREFERENCES,0);
                refreshSubMenu(menuItem.getSubMenu(),selected);
                return true;
            }
        }

        presenter.setPeriodType(itemId);
        presenter.generatePeriodTitle(dateFormat);

        invalidate();

        saveSharedPreference(itemId,  presenter.getPeriodTitle()
                , presenter.getStartDate().getTime()
                , presenter.getFinishDate().getTime()
                ,null);
        return super.onOptionsItemSelected(menuItem);
     }

     void showAbout() {
         AlertDialog.Builder about = new AlertDialog.Builder(this);
         LayoutInflater layoutInflater = getLayoutInflater();
         about.setView(layoutInflater.inflate(R.layout.about_dialog, null,false));
         about.setTitle(R.string.action_info_about);
         about.create().show();
     }

     @Override
     public boolean onPrepareOptionsMenu(Menu menu) {

        int selectedPeriodType = sp.getInt(Constants.PERIOD_TYPE_PREFERENCES,0);
        MenuItem period = menu.findItem(R.id.action_period);
        SubMenu periodSubMenu = period.getSubMenu();

        refreshSubMenu(periodSubMenu, selectedPeriodType);

        return super.onPrepareOptionsMenu(menu);
    }

    public void onShowStores(ArrayList<Store> storeItems) {
        this.storesAdapter.clear();
        this.storesAdapter.addAll(storeItems);
    }

    void refreshSubMenu(SubMenu subMenu, int selected) {
        for (int i = 0; i < subMenu.size(); i++) {
            if (subMenu.getItem(i).getItemId() != selected) {
                subMenu.getItem(i).setCheckable(false);
                subMenu.getItem(i).setTitle(getTitle(subMenu.getItem(i).getItemId()));
            } else {
                subMenu.getItem(i).setCheckable(true);
                subMenu.getItem(i).setChecked(true);
                String currentPeriodTitle = presenter.periodTitle == null ?
                        sp.getString(Constants.PERIOD_TITLE_PREFERENCES, "") :
                        presenter.periodTitle;
                MenuItem item = subMenu.getItem(i);
                item.setTitle(String.format("%s (%s)",
                        getTitle(subMenu.getItem(i).getItemId()),currentPeriodTitle));
            }
        }
    }

    protected Disposable respondToStoreFilterClick() {
        return itemStoreClicks().subscribe(new Consumer<String>() {
            @Override
            public void accept(String storeId) throws Exception {
                changeChosenStore(storeId);
            }
        });
    }

    public void saveSharedPreference(int typeId,
                                     String periodTitle,
                                     long periodStart,
                                     long periodFinish,
                                     ArrayList<String> chosenStoreList) {

    }

    protected void setupWindowAnimations() {
    }

    public void onDateUnselected(Date date) {

    }

    public void onDateSelected(Date date) {
        if (dialogView.getSelectedDates().size() <= 1) {
            if (dialogView.getSelectedDates().size() != 1 || previousDate != date) {
                previousDate = date;
            }
        }
        presenter.setStartDate((Date) dialogView.getSelectedDates().get(0));

        Date todayDate = Calendar.getInstance(Locale.getDefault()).getTime();
        Date lastDate = (Date) dialogView.getSelectedDates().get(dialogView.getSelectedDates().size() - 1);
        Calendar last = Calendar.getInstance(Locale.getDefault());
        last.setTime(lastDate);
        Calendar today = Calendar.getInstance(Locale.getDefault());
        today.setTime(todayDate);
        if (last.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            lastDate = todayDate;
        } else {
            lastDate = Utils.getEndOfADay(lastDate);
        }
        presenter.setFinishDate(lastDate);

        presenter.setPeriodTitle(presenter.generatePeriodTitle(dateFormat));
        invalidate();

        saveSharedPreference(R.id.action_custom_period_filter,
                presenter.getPeriodTitle(),
                presenter.getStartDate().getTime(),
                presenter.getFinishDate().getTime(),
                null);
        this.previousDate = date;
    }

    public void onInvalidDateSelected(Date date) {

    }

    public void onShow(DialogInterface dialogInterface) {
        dialogView.fixDialogDimens();
    }

    public void showCalendarInDialog() {

        Calendar periodFrom = Calendar.getInstance(Locale.getDefault());
        periodFrom.add(Calendar.YEAR, -3);
        Calendar periodTo = java.util.Calendar.getInstance(Locale.getDefault());
        periodTo.add(Calendar.DATE, 1);

        dialogView = (CalendarPickerView)LayoutInflater.from(this).inflate(R.layout.pick_data_dialog, null, false);
        dialogView.setCustomDayView(new DefaultDayViewAdapter());
        dialogView.setOnDateSelectedListener(this);
        dialogView.setOnInvalidDateSelectedListener(this);
        dialogView.setDecorators(Collections.emptyList());
        dialogView.init(Utils.getStartOfADay(periodFrom.getTime()), periodTo.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);

        if (presenter.getStartDate() != null) {
            dialogView.selectDate(presenter.getStartDate());
        }
        if (presenter.getFinishDate() != null) {
            dialogView.selectDate(presenter.getFinishDate());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_choose_period_title));
        builder.setView(dialogView);
        builder.setNeutralButton("Закрыть", null);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);
        dialog.show();
    }

    public void showStores() {
        //ListView view = (ListView)LayoutInflater.from(this).inflate(R.layout.store_list_dialog, null, false);
        //view.setAdapter(storesAdapter);
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_choose_store_title))
                .setAdapter(storesAdapter,null)
                //.setView(view)
                .setNeutralButton("Закрыть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
        }).create().show();
    }



}
