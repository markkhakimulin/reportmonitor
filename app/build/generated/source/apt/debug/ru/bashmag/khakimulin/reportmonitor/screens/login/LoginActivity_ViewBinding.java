// Generated code from Butter Knife. Do not modify!
package ru.bashmag.khakimulin.reportmonitor.screens.login;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import ru.bashmag.khakimulin.reportmonitor.R;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target, View source) {
    this.target = target;

    target.progressBar = Utils.findRequiredViewAsType(source, R.id.preloader, "field 'progressBar'", ProgressBar.class);
    target.loginButton = Utils.findRequiredViewAsType(source, R.id.loginButton, "field 'loginButton'", Button.class);
    target.logo = Utils.findRequiredViewAsType(source, R.id.image_logo, "field 'logo'", ImageView.class);
    target.loginContent = Utils.findRequiredView(source, R.id.login_content, "field 'loginContent'");
    target.loginContentControls = Utils.findRequiredView(source, R.id.login_content_controls, "field 'loginContentControls'");
    target.loginText = Utils.findRequiredViewAsType(source, R.id.loginText, "field 'loginText'", AutoCompleteTextView.class);
    target.passwordText = Utils.findRequiredViewAsType(source, R.id.passText, "field 'passwordText'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.progressBar = null;
    target.loginButton = null;
    target.logo = null;
    target.loginContent = null;
    target.loginContentControls = null;
    target.loginText = null;
    target.passwordText = null;
  }
}
