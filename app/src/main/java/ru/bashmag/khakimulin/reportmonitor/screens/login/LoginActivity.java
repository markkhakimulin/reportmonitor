package ru.bashmag.khakimulin.reportmonitor.screens.login;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import ru.bashmag.khakimulin.reportmonitor.App;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.core.BaseActivity;
import ru.bashmag.khakimulin.reportmonitor.db.tables.User;
import ru.bashmag.khakimulin.reportmonitor.screens.login.di.DaggerLoginComponent;
import ru.bashmag.khakimulin.reportmonitor.screens.login.di.LoginModule;
import ru.bashmag.khakimulin.reportmonitor.screens.login.mvp.LoginPresenter;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportActivity;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;

/**
 * Created by Mark Khakimulin on 02.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class LoginActivity extends BaseActivity {


    @BindView(R.id.preloader)
    ProgressBar progressBar;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.image_logo)
    ImageView logo;

    @BindView(R.id.login_content)
    View loginContent;

    @BindView(R.id.login_content_controls)
    View loginContentControls;

    @BindView(R.id.loginText)
    AutoCompleteTextView loginText;

    @BindView(R.id.passText)
    EditText passwordText;


    @Inject
    LoginPresenter loginPresenter;

    ArrayAdapter<User> userAdapter;
    User userData;

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
        loginText.setFocusable(false);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginPresenter.refresh();

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login = loginText.getText().toString();
                String pass = passwordText.getText().toString();

                if (login.isEmpty()) {
                    loginText.setError("Не заполнено");
                    loginText.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    passwordText.setError("Не заполнено");
                    passwordText.requestFocus();
                    return;
                }

                loginText.clearFocus();
                passwordText.clearFocus();
                showButtons(false);

                try {

                    if (userData.getId().equals(Constants.EMPTY_ID)) {
                        loginPresenter.loginAnonymous(userData.getId(),pass);
                    } else {
                        byte[] data = String.format("%s:%s",userData.getId(),pass).getBytes("UTF-8");
                        loginPresenter.login(Base64.encodeToString(data,2));
                    }

                } catch (UnsupportedEncodingException e) {
                    onShowToast(e.getMessage());
                }
            }
        });

        loginPresenter.onCreate();

        @NonNull
        String userId = "";
        if (getIntent().hasExtra(Constants.USER_ID)) {
            userId = getIntent().getStringExtra(Constants.USER_ID);
        } else {
            userId = Objects.requireNonNull(sp.getString(Constants.USER_ID, Constants.EMPTY_ID));
        }

        if (savedInstanceState != null) {
            loginText.setText(savedInstanceState.getString("loginText"));
            passwordText.setText(savedInstanceState.getString("passwordText"));
            userId = Objects.requireNonNull(savedInstanceState.getString(Constants.USER_ID,""));
        }

        if (userData == null && !userId.isEmpty()) {
            loginPresenter.getLoginById(userId);
        }

        loginButton.requestFocus();
    }


    @Override
    protected void setupWindowAnimations() {

    }
    protected void invalidate() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        showButtons(true);
    }

    public void showUser(User data) {
        this.userData = data;
        loginText.setText(data.getDescription());
    }

    public void showUserList(List<User> list) {
        userAdapter = new ArrayAdapter<User>(LoginActivity.this,android.R.layout.simple_dropdown_item_1line,list);
        loginText.setAdapter(userAdapter);
        loginText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userData = userAdapter.getItem(position);
            }
        });
        loginText.showDropDown();
    }


    @Override
    protected void resolveDaggerDependency() {

        DaggerLoginComponent.builder().
                appComponent(App.getAppComponent()).
                loginModule(new LoginModule(this)).
                build().inject(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    public void showReportListActivity(User user) {

        SharedPreferences.Editor edit = sp.edit();
        edit.putString(Constants.USER_ID_PREFERENCES,user.getId());
        edit.putString(Constants.USER_TITLE_PREFERENCES,user.getDescription());
        edit.apply();

        Intent i = new Intent(this, TurnoverReportActivity.class);
        i.putExtra(Constants.USER_ID,user.getId());
        i.putExtra(Constants.USER_TITLE,user.getDescription());
        startActivity(i);
        finish();
    }
    public void showButtons(Boolean show) {
        loginContentControls.setVisibility(show?View.VISIBLE:View.INVISIBLE);
        progressBar.setVisibility(!show?View.VISIBLE:View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void gotoVPNSettings() {
        startActivity(new Intent(Settings.ACTION_VPN_SETTINGS));
    }

    public void gotoNETSettings() {
        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loginPresenter.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("loginText",loginText.getText().toString());
        outState.putString("passwordText",passwordText.getText().toString());
        if (userData != null)
        outState.putString(Constants.USER_ID,userData.getId());
        super.onSaveInstanceState(outState);
    }


}
