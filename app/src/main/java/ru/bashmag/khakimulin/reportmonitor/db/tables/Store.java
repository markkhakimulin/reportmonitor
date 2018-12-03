package ru.bashmag.khakimulin.reportmonitor.db.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Mark Khakimulin on 28.09.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Entity
public class Store {

    public int actual;
    public String code;
    public String description;
    @PrimaryKey
    @NonNull
    public String id;
    public long last;
    @Nullable
    public long loaded;
    public int marked;
    public long unloaded;
    @ColumnInfo(name = "user_id")
    public String userId;

    public String toString() {
        return this.description;
    }
}
