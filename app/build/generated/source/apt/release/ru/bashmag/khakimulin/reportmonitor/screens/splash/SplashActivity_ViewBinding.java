// Generated code from Butter Knife. Do not modify!
package ru.bashmag.khakimulin.reportmonitor.screens.splash;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import ru.bashmag.khakimulin.reportmonitor.R;

public class SplashActivity_ViewBinding implements Unbinder {
  private SplashActivity target;

  @UiThread
  public SplashActivity_ViewBinding(SplashActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SplashActivity_ViewBinding(SplashActivity target, View source) {
    this.target = target;

    target.mContentView = Utils.findRequiredView(source, R.id.fullscreen_content, "field 'mContentView'");
    target.mControlsView = Utils.findRequiredView(source, R.id.fullscreen_content_controls, "field 'mControlsView'");
    target.textStatus = Utils.findRequiredViewAsType(source, R.id.text_status_splash, "field 'textStatus'", TextView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.preloader, "field 'progressBar'", ProgressBar.class);
    target.button = Utils.findRequiredViewAsType(source, R.id.button, "field 'button'", Button.class);
    target.logo = Utils.findRequiredViewAsType(source, R.id.image_logo, "field 'logo'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SplashActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mContentView = null;
    target.mControlsView = null;
    target.textStatus = null;
    target.progressBar = null;
    target.button = null;
    target.logo = null;
  }
}
