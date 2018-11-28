package ru.bashmag.khakimulin.reportmonitor.db.dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.util.StringUtil;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.db.tables.UserStore;

@SuppressWarnings("unchecked")
public class UserStoreDao_Impl implements UserStoreDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfUserStore;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfUserStore;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfUserStore;

  public UserStoreDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserStore = new EntityInsertionAdapter<UserStore>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `user_store`(`user_id`,`id`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserStore value) {
        if (value.user_id == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.user_id);
        }
        if (value.id == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.id);
        }
      }
    };
    this.__deletionAdapterOfUserStore = new EntityDeletionOrUpdateAdapter<UserStore>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `user_store` WHERE `user_id` = ? AND `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserStore value) {
        if (value.user_id == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.user_id);
        }
        if (value.id == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.id);
        }
      }
    };
    this.__updateAdapterOfUserStore = new EntityDeletionOrUpdateAdapter<UserStore>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `user_store` SET `user_id` = ?,`id` = ? WHERE `user_id` = ? AND `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserStore value) {
        if (value.user_id == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.user_id);
        }
        if (value.id == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.id);
        }
        if (value.user_id == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.user_id);
        }
        if (value.id == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.id);
        }
      }
    };
  }

  @Override
  public void insert(UserStore user) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserStore.insert(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(UserStore user) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfUserStore.handle(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(List<UserStore> list) {
    __db.beginTransaction();
    try {
      __updateAdapterOfUserStore.handleMultiple(list);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Store> getAllStores(String userId) {
    final String _sql = "SELECT * FROM store as s left join (select * from user_store as us where us.user_id = ?) us on s.id = us.id order by description";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfCode = _cursor.getColumnIndexOrThrow("code");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfMarked = _cursor.getColumnIndexOrThrow("marked");
      final int _cursorIndexOfLoaded = _cursor.getColumnIndexOrThrow("loaded");
      final int _cursorIndexOfUnloaded = _cursor.getColumnIndexOrThrow("unloaded");
      final int _cursorIndexOfLast = _cursor.getColumnIndexOrThrow("last");
      final int _cursorIndexOfActual = _cursor.getColumnIndexOrThrow("actual");
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfUserId_1 = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfId_1 = _cursor.getColumnIndexOrThrow("id");
      final List<Store> _result = new ArrayList<Store>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Store _item;
        _item = new Store();
        _item.id = _cursor.getString(_cursorIndexOfId);
        _item.code = _cursor.getString(_cursorIndexOfCode);
        _item.description = _cursor.getString(_cursorIndexOfDescription);
        _item.marked = _cursor.getInt(_cursorIndexOfMarked);
        _item.loaded = _cursor.getLong(_cursorIndexOfLoaded);
        _item.unloaded = _cursor.getLong(_cursorIndexOfUnloaded);
        _item.last = _cursor.getLong(_cursorIndexOfLast);
        _item.actual = _cursor.getInt(_cursorIndexOfActual);
        _item.userId = _cursor.getString(_cursorIndexOfUserId);
        _item.userId = _cursor.getString(_cursorIndexOfUserId_1);
        _item.id = _cursor.getString(_cursorIndexOfId_1);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> getAllStoreIds(String userId) {
    final String _sql = "SELECT s.id FROM store as s left join (select * from user_store as us where us.user_id = ?) us on s.id = us.id order by description";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        _item = _cursor.getString(0);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public void deleteAllExcept(String userId, List<String> ids) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("DELETE from user_store where user_store.user_id = ");
    _stringBuilder.append("?");
    _stringBuilder.append(" and user_store.id not in (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
    int _argIndex = 1;
    if (userId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    for (String _item : ids) {
      if (_item == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }
}
