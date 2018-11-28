package ru.bashmag.khakimulin.reportmonitor.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.bashmag.khakimulin.reportmonitor.db.dao.StoreDao;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;

/**
 * Created by Mark Khakimulin on 28.09.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Database(entities = {Store.class}, version = 1,exportSchema = false)
public abstract class DB extends RoomDatabase {
    public abstract StoreDao storeDao();
}

