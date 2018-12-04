package ru.bashmag.khakimulin.reportmonitor.screens.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.transition.ChangeImageTransform;
import android.transition.Fade;
import android.transition.TransitionSet;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import ru.bashmag.khakimulin.reportmonitor.App;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.login.LoginActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.di.DaggerSplashComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.di.SplashModule;
import ru.bashmag.khakimulin.reportmonitor.screens.splash.mvp.SplashPresenter;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class SplashActivity extends BaseActivity {

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();


    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @BindView(R.id.fullscreen_content)
    View mContentView;

    @BindView(R.id.fullscreen_content_controls)
    View mControlsView;

    @BindView(R.id.text_status_splash)
    TextView textStatus;

    @BindView(R.id.button_repeat)
    Button button;

    @BindView(R.id.image_logo)
    ImageView logo;

    @BindView(R.id.preloader)
    ProgressBar progressBar;

    @Inject
    SplashPresenter splashPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invalidate();
                splashPresenter.refresh();
            }
        });
        splashPresenter.onCreate();
    }

    @Override
    protected void resolveDaggerDependency() {

        DaggerSplashComponent.builder().
                appComponent(App.getAppComponent()).
                splashModule(new SplashModule(this)).
                build().inject(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    protected void invalidate() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.button.setVisibility(View.GONE);
    }

    public void showButton() {
        this.progressBar.setVisibility(View.GONE);
        this.button.setVisibility(View.VISIBLE);
    }

    public void showLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(this, logo,
                Objects.requireNonNull(ViewCompat.getTransitionName(logo))).toBundle());
    }

    public void showReportListActivity() {
        String userId = this.sp.getString(Constants.USER_ID_PREFERENCES, Constants.EMPTY_ID);
        String userTitle = this.sp.getString(Constants.USER_TITLE_PREFERENCES, getString(R.string.anonymous));
        if (userId.equalsIgnoreCase(Constants.EMPTY_ID)) {
            showLoginActivity();
            return;
        }
        Intent i = new Intent(this, TurnoverReportActivity.class);
        i.putExtra(Constants.USER_ID, userId);
        i.putExtra(Constants.USER_TITLE, userTitle);
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        splashPresenter.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        splashPresenter.refresh();
    }

    public void setStatus(String state) {
        this.textStatus.setText(state);
    }

    protected void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            TransitionSet set = new TransitionSet();
            Fade fade = new Fade();
            fade.setDuration(getResources().getInteger(R.integer.fade_time));
            set.addTransition(new Fade());
            set.addTransition(new ChangeImageTransform());
            getWindow().setEnterTransition(set);
        }
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

}
