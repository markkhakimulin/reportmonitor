package ru.bashmag.khakimulin.reportmonitor.core.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.Objects;

import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Mark Khakimulin on 15.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class StoresAdapter extends ArrayAdapter<Store>{

    private final PublishSubject<String> itemClicks = PublishSubject.create();
    public StoresAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_multiple_choice, android.R.id.text1);
    }

    public Observable<String> observeClicks() {
        return itemClicks;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Store store = Objects.requireNonNull(getItem(position));

        CheckedTextView view = (CheckedTextView) super.getView(position,convertView,parent);
        view.setChecked(store.marked > 0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (store.marked > 0) {
                    view.setChecked(false);
                    store.marked = 0;
                } else {
                    view.setChecked(true);
                    store.marked = 1;
                }

                itemClicks.onNext(store.id);
            }
        });

        return view;
    }
}
