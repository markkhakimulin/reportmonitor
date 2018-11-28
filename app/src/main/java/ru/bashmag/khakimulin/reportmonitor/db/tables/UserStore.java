package ru.bashmag.khakimulin.reportmonitor.db.tables;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Mark Khakimulin on 12.11.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Entity(tableName = "user_store",primaryKeys = { "user_id", "id" },
        indices = @Index(value = {"user_id", "id"}))
public class UserStore {

    @NonNull
    public String user_id;
    @NonNull
    public String id;

    public UserStore() {

    }

    public UserStore(@NonNull String userId, @NonNull String storeId) {
        user_id = userId;
        id = storeId;
    }
}
