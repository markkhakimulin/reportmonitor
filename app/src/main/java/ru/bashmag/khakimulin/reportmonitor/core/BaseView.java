package ru.bashmag.khakimulin.reportmonitor.core;

import android.os.Bundle;

public interface BaseView {
    void onCreate(Bundle savedInstanceState);
    void onDestroy();
    void onSaveInstanceState(Bundle outState);
    void onRestoreInstanceState(Bundle savedInstanceState);
}
