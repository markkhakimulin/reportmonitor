package ru.bashmag.khakimulin.reportmonitor.core;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.Callable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.fragments.ConversionView;
import ru.bashmag.khakimulin.reportmonitor.utils.Utils;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public abstract class BaseActivity extends AppCompatActivity{

    private ProgressDialog mProgressDialog;
    private Unbinder binder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resolveDaggerDependency();

        setContentView(getContentView());
        binder = ButterKnife.bind(this);
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

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
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

}
