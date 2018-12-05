package ru.bashmag.khakimulin.reportmonitor.utils.rx;

import io.reactivex.Scheduler;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public interface RxSchedulers {


    Scheduler runOnBackground();

    Scheduler io();

    Scheduler compute();

    Scheduler androidThread();

    Scheduler internet();



}