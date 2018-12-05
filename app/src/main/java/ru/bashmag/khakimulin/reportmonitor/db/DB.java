package ru.bashmag.khakimulin.reportmonitor.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.bashmag.khakimulin.reportmonitor.db.dao.StoreDao;
import ru.bashmag.khakimulin.reportmonitor.db.dao.UserChosenStoresDao;
import ru.bashmag.khakimulin.reportmonitor.db.dao.UserDao;
import ru.bashmag.khakimulin.reportmonitor.db.dao.UserStoreDao;
import ru.bashmag.khakimulin.reportmonitor.db.tables.ChosenStore;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.db.tables.User;
import ru.bashmag.khakimulin.reportmonitor.db.tables.UserStore;

/**
 * Created by Mark Khakimulin on 28.09.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Database(entities = {Store.class, User.class, ChosenStore.class, UserStore.class}, exportSchema = false, version = 9)
public abstract class DB extends RoomDatabase {
    public abstract UserChosenStoresDao chosenStoreDao();

    public abstract StoreDao storeDao();

    public abstract UserDao userDao();

    public abstract UserStoreDao userStoreDao();
}
