package ru.bashmag.khakimulin.reportmonitor.db.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Mark Khakimulin on 12.11.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Entity(tableName = "user_store_join",primaryKeys = { "user_id", "id", })
public class ChosenStore {

    @NonNull
    public String user_id;
    @NonNull
    public String id;

    public ChosenStore () {

    }

    public ChosenStore (@NonNull String userId,@NonNull String storeId) {
        user_id = userId;
        id = storeId;
    }
}
