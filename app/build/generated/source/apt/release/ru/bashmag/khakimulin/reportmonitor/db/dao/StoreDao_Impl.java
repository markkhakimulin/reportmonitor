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

@SuppressWarnings("unchecked")
public class StoreDao_Impl implements StoreDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfStore;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfStore;

  public StoreDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStore = new EntityInsertionAdapter<Store>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Store`(`id`,`code`,`description`,`marked`,`loaded`,`unloaded`,`last`,`actual`,`user_id`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Store value) {
        if (value.id == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.id);
        }
        if (value.code == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.code);
        }
        if (value.description == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.description);
        }
        stmt.bindLong(4, value.marked);
        stmt.bindLong(5, value.loaded);
        stmt.bindLong(6, value.unloaded);
        stmt.bindLong(7, value.last);
        stmt.bindLong(8, value.actual);
        if (value.userId == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.userId);
        }
      }
    };
    this.__deletionAdapterOfStore = new EntityDeletionOrUpdateAdapter<Store>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Store` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Store value) {
        if (value.id == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.id);
        }
      }
    };
  }

  @Override
  public void insert(Store store) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfStore.insert(store);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll(List<Store> storeList) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfStore.handleMultiple(storeList);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Store store) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfStore.handle(store);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Store> getAll() {
    final String _sql = "SELECT * FROM store order by description";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Store getById(String storeId) {
    final String _sql = "SELECT * FROM store WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (storeId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, storeId);
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
      final Store _result;
      if(_cursor.moveToFirst()) {
        _result = new Store();
        _result.id = _cursor.getString(_cursorIndexOfId);
        _result.code = _cursor.getString(_cursorIndexOfCode);
        _result.description = _cursor.getString(_cursorIndexOfDescription);
        _result.marked = _cursor.getInt(_cursorIndexOfMarked);
        _result.loaded = _cursor.getLong(_cursorIndexOfLoaded);
        _result.unloaded = _cursor.getLong(_cursorIndexOfUnloaded);
        _result.last = _cursor.getLong(_cursorIndexOfLast);
        _result.actual = _cursor.getInt(_cursorIndexOfActual);
        _result.userId = _cursor.getString(_cursorIndexOfUserId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Store> getByUserId(String userId) {
    final String _sql = "SELECT * FROM store inner join user_store as us on store.id = us.id WHERE us.user_id = ?";
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
  public List<Store> getTop3() {
    final String _sql = "SELECT * FROM store limit 3";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public void deleteAllExcept(List<String> storeList) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("delete FROM store WHERE store.id not in (");
    final int _inputSize = storeList.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
    int _argIndex = 1;
    for (String _item : storeList) {
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
