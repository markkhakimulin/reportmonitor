package ru.bashmag.khakimulin.reportmonitor.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import ru.bashmag.khakimulin.reportmonitor.db.dao.StoreDao;
import ru.bashmag.khakimulin.reportmonitor.db.dao.StoreDao_Impl;
import ru.bashmag.khakimulin.reportmonitor.db.dao.UserChosenStoresDao;
import ru.bashmag.khakimulin.reportmonitor.db.dao.UserChosenStoresDao_Impl;
import ru.bashmag.khakimulin.reportmonitor.db.dao.UserDao;
import ru.bashmag.khakimulin.reportmonitor.db.dao.UserDao_Impl;
import ru.bashmag.khakimulin.reportmonitor.db.dao.UserStoreDao;
import ru.bashmag.khakimulin.reportmonitor.db.dao.UserStoreDao_Impl;

@SuppressWarnings("unchecked")
public class DB_Impl extends DB {
  private volatile StoreDao _storeDao;

  private volatile UserDao _userDao;

  private volatile UserChosenStoresDao _userChosenStoresDao;

  private volatile UserStoreDao _userStoreDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(8) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Store` (`id` TEXT NOT NULL, `code` TEXT, `description` TEXT, `marked` INTEGER NOT NULL, `loaded` INTEGER NOT NULL, `unloaded` INTEGER NOT NULL, `last` INTEGER NOT NULL, `actual` INTEGER NOT NULL, `user_id` TEXT, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `User` (`id` TEXT NOT NULL, `code` TEXT, `description` TEXT, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `user_store_join` (`user_id` TEXT NOT NULL, `id` TEXT NOT NULL, PRIMARY KEY(`user_id`, `id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `user_store` (`user_id` TEXT NOT NULL, `id` TEXT NOT NULL, PRIMARY KEY(`user_id`, `id`))");
        _db.execSQL("CREATE  INDEX `index_user_store_user_id_id` ON `user_store` (`user_id`, `id`)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"18122accbbed23ea69db94c692c0b6fa\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Store`");
        _db.execSQL("DROP TABLE IF EXISTS `User`");
        _db.execSQL("DROP TABLE IF EXISTS `user_store_join`");
        _db.execSQL("DROP TABLE IF EXISTS `user_store`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsStore = new HashMap<String, TableInfo.Column>(9);
        _columnsStore.put("id", new TableInfo.Column("id", "TEXT", true, 1));
        _columnsStore.put("code", new TableInfo.Column("code", "TEXT", false, 0));
        _columnsStore.put("description", new TableInfo.Column("description", "TEXT", false, 0));
        _columnsStore.put("marked", new TableInfo.Column("marked", "INTEGER", true, 0));
        _columnsStore.put("loaded", new TableInfo.Column("loaded", "INTEGER", true, 0));
        _columnsStore.put("unloaded", new TableInfo.Column("unloaded", "INTEGER", true, 0));
        _columnsStore.put("last", new TableInfo.Column("last", "INTEGER", true, 0));
        _columnsStore.put("actual", new TableInfo.Column("actual", "INTEGER", true, 0));
        _columnsStore.put("user_id", new TableInfo.Column("user_id", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStore = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStore = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStore = new TableInfo("Store", _columnsStore, _foreignKeysStore, _indicesStore);
        final TableInfo _existingStore = TableInfo.read(_db, "Store");
        if (! _infoStore.equals(_existingStore)) {
          throw new IllegalStateException("Migration didn't properly handle Store(ru.bashmag.khakimulin.reportmonitor.db.tables.Store).\n"
                  + " Expected:\n" + _infoStore + "\n"
                  + " Found:\n" + _existingStore);
        }
        final HashMap<String, TableInfo.Column> _columnsUser = new HashMap<String, TableInfo.Column>(3);
        _columnsUser.put("id", new TableInfo.Column("id", "TEXT", true, 1));
        _columnsUser.put("code", new TableInfo.Column("code", "TEXT", false, 0));
        _columnsUser.put("description", new TableInfo.Column("description", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUser = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUser = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUser = new TableInfo("User", _columnsUser, _foreignKeysUser, _indicesUser);
        final TableInfo _existingUser = TableInfo.read(_db, "User");
        if (! _infoUser.equals(_existingUser)) {
          throw new IllegalStateException("Migration didn't properly handle User(ru.bashmag.khakimulin.reportmonitor.db.tables.User).\n"
                  + " Expected:\n" + _infoUser + "\n"
                  + " Found:\n" + _existingUser);
        }
        final HashMap<String, TableInfo.Column> _columnsUserStoreJoin = new HashMap<String, TableInfo.Column>(2);
        _columnsUserStoreJoin.put("user_id", new TableInfo.Column("user_id", "TEXT", true, 1));
        _columnsUserStoreJoin.put("id", new TableInfo.Column("id", "TEXT", true, 2));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserStoreJoin = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserStoreJoin = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserStoreJoin = new TableInfo("user_store_join", _columnsUserStoreJoin, _foreignKeysUserStoreJoin, _indicesUserStoreJoin);
        final TableInfo _existingUserStoreJoin = TableInfo.read(_db, "user_store_join");
        if (! _infoUserStoreJoin.equals(_existingUserStoreJoin)) {
          throw new IllegalStateException("Migration didn't properly handle user_store_join(ru.bashmag.khakimulin.reportmonitor.db.tables.ChosenStore).\n"
                  + " Expected:\n" + _infoUserStoreJoin + "\n"
                  + " Found:\n" + _existingUserStoreJoin);
        }
        final HashMap<String, TableInfo.Column> _columnsUserStore = new HashMap<String, TableInfo.Column>(2);
        _columnsUserStore.put("user_id", new TableInfo.Column("user_id", "TEXT", true, 1));
        _columnsUserStore.put("id", new TableInfo.Column("id", "TEXT", true, 2));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserStore = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserStore = new HashSet<TableInfo.Index>(1);
        _indicesUserStore.add(new TableInfo.Index("index_user_store_user_id_id", false, Arrays.asList("user_id","id")));
        final TableInfo _infoUserStore = new TableInfo("user_store", _columnsUserStore, _foreignKeysUserStore, _indicesUserStore);
        final TableInfo _existingUserStore = TableInfo.read(_db, "user_store");
        if (! _infoUserStore.equals(_existingUserStore)) {
          throw new IllegalStateException("Migration didn't properly handle user_store(ru.bashmag.khakimulin.reportmonitor.db.tables.UserStore).\n"
                  + " Expected:\n" + _infoUserStore + "\n"
                  + " Found:\n" + _existingUserStore);
        }
      }
    }, "18122accbbed23ea69db94c692c0b6fa", "55701155e5658ca9640caf4bef8ec36d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Store","User","user_store_join","user_store");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Store`");
      _db.execSQL("DELETE FROM `User`");
      _db.execSQL("DELETE FROM `user_store_join`");
      _db.execSQL("DELETE FROM `user_store`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public StoreDao storeDao() {
    if (_storeDao != null) {
      return _storeDao;
    } else {
      synchronized(this) {
        if(_storeDao == null) {
          _storeDao = new StoreDao_Impl(this);
        }
        return _storeDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public UserChosenStoresDao chosenStoreDao() {
    if (_userChosenStoresDao != null) {
      return _userChosenStoresDao;
    } else {
      synchronized(this) {
        if(_userChosenStoresDao == null) {
          _userChosenStoresDao = new UserChosenStoresDao_Impl(this);
        }
        return _userChosenStoresDao;
      }
    }
  }

  @Override
  public UserStoreDao userStoreDao() {
    if (_userStoreDao != null) {
      return _userStoreDao;
    } else {
      synchronized(this) {
        if(_userStoreDao == null) {
          _userStoreDao = new UserStoreDao_Impl(this);
        }
        return _userStoreDao;
      }
    }
  }
}
