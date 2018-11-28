package ru.bashmag.khakimulin.reportmonitor.db.tables;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by Mark Khakimulin on 28.09.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Entity
public class Store {

    @NonNull
    @PrimaryKey
    public String id;

    public String code;

    public String description;

    public int marked;

    @Override
    public String toString() {
        return description;
    }
}
