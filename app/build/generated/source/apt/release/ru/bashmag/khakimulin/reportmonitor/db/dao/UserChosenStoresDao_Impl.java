package ru.bashmag.khakimulin.reportmonitor.db.dao;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.arch.persistence.room.util.StringUtil;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import ru.bashmag.khakimulin.reportmonitor.db.tables.ChosenStore;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;

@SuppressWarnings("unchecked")
public class UserChosenStoresDao_Impl implements UserChosenStoresDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfChosenStore;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public UserChosenStoresDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfChosenStore = new EntityInsertionAdapter<ChosenStore>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `user_store_join`(`user_id`,`id`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ChosenStore value) {
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
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM user_store_join WHERE user_store_join.user_id = ? AND user_store_join.id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(ChosenStore chosenStore) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfChosenStore.insert(chosenStore);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(String userId, String storeId) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      if (userId == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, userId);
      }
      _argIndex = 2;
      if (storeId == null) {
        _stmt.bindNull(_argIndex);
      } else {
        _stmt.bindString(_argIndex, storeId);
      }
      final int _result = _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
      __preparedStmtOfDelete.release(_stmt);
    }
  }

  @Override
  public List<Store> getAllByAnonymous() {
    final String _sql = "SELECT s.user_id,s.description,s.code,s.id,s.last,s.loaded,s.unloaded,s.actual,CASE WHEN usj.id IS NULL THEN 0 ELSE 1 END as marked FROM store as s Left JOIN (select * from user_store_join  as uj where uj.user_id = '00000000-0000-0000-0000-000000000000') as usj ON s.id=usj.id order by s.description";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfCode = _cursor.getColumnIndexOrThrow("code");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfLast = _cursor.getColumnIndexOrThrow("last");
      final int _cursorIndexOfLoaded = _cursor.getColumnIndexOrThrow("loaded");
      final int _cursorIndexOfUnloaded = _cursor.getColumnIndexOrThrow("unloaded");
      final int _cursorIndexOfActual = _cursor.getColumnIndexOrThrow("actual");
      final int _cursorIndexOfMarked = _cursor.getColumnIndexOrThrow("marked");
      final List<Store> _result = new ArrayList<Store>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Store _item;
        _item = new Store();
        _item.userId = _cursor.getString(_cursorIndexOfUserId);
        _item.description = _cursor.getString(_cursorIndexOfDescription);
        _item.code = _cursor.getString(_cursorIndexOfCode);
        _item.id = _cursor.getString(_cursorIndexOfId);
        _item.last = _cursor.getLong(_cursorIndexOfLast);
        _item.loaded = _cursor.getLong(_cursorIndexOfLoaded);
        _item.unloaded = _cursor.getLong(_cursorIndexOfUnloaded);
        _item.actual = _cursor.getInt(_cursorIndexOfActual);
        _item.marked = _cursor.getInt(_cursorIndexOfMarked);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Store> getAllByIdsByAnonymous(List<String> chosenIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT s.user_id,s.description,s.code,s.id,s.last,s.loaded,s.unloaded,s.actual,CASE WHEN usj.id IS NULL THEN 0 ELSE 1 END as marked FROM store as s Left JOIN (select * from store  as us where us.id in (");
    final int _inputSize = chosenIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")) as usj ON s.id=usj.id order by s.description");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : chosenIds) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfCode = _cursor.getColumnIndexOrThrow("code");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfLast = _cursor.getColumnIndexOrThrow("last");
      final int _cursorIndexOfLoaded = _cursor.getColumnIndexOrThrow("loaded");
      final int _cursorIndexOfUnloaded = _cursor.getColumnIndexOrThrow("unloaded");
      final int _cursorIndexOfActual = _cursor.getColumnIndexOrThrow("actual");
      final int _cursorIndexOfMarked = _cursor.getColumnIndexOrThrow("marked");
      final List<Store> _result = new ArrayList<Store>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Store _item_1;
        _item_1 = new Store();
        _item_1.userId = _cursor.getString(_cursorIndexOfUserId);
        _item_1.description = _cursor.getString(_cursorIndexOfDescription);
        _item_1.code = _cursor.getString(_cursorIndexOfCode);
        _item_1.id = _cursor.getString(_cursorIndexOfId);
        _item_1.last = _cursor.getLong(_cursorIndexOfLast);
        _item_1.loaded = _cursor.getLong(_cursorIndexOfLoaded);
        _item_1.unloaded = _cursor.getLong(_cursorIndexOfUnloaded);
        _item_1.actual = _cursor.getInt(_cursorIndexOfActual);
        _item_1.marked = _cursor.getInt(_cursorIndexOfMarked);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Store> getAllByUserId(String userId) {
    final String _sql = "SELECT s.user_id,s.description,s.code,s.id,s.last,s.loaded,s.unloaded,s.actual,CASE WHEN usj.id IS NULL THEN 0 ELSE 1 END as marked FROM store as s Left JOIN (select * from user_store_join  as uj where uj.user_id = ?) as usj ON s.id=usj.id Inner JOIN user_store as us ON s.id=us.id WHERE us.user_id=?  order by s.description";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfCode = _cursor.getColumnIndexOrThrow("code");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfLast = _cursor.getColumnIndexOrThrow("last");
      final int _cursorIndexOfLoaded = _cursor.getColumnIndexOrThrow("loaded");
      final int _cursorIndexOfUnloaded = _cursor.getColumnIndexOrThrow("unloaded");
      final int _cursorIndexOfActual = _cursor.getColumnIndexOrThrow("actual");
      final int _cursorIndexOfMarked = _cursor.getColumnIndexOrThrow("marked");
      final List<Store> _result = new ArrayList<Store>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Store _item;
        _item = new Store();
        _item.userId = _cursor.getString(_cursorIndexOfUserId);
        _item.description = _cursor.getString(_cursorIndexOfDescription);
        _item.code = _cursor.getString(_cursorIndexOfCode);
        _item.id = _cursor.getString(_cursorIndexOfId);
        _item.last = _cursor.getLong(_cursorIndexOfLast);
        _item.loaded = _cursor.getLong(_cursorIndexOfLoaded);
        _item.unloaded = _cursor.getLong(_cursorIndexOfUnloaded);
        _item.actual = _cursor.getInt(_cursorIndexOfActual);
        _item.marked = _cursor.getInt(_cursorIndexOfMarked);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Store> getAllByIdsByUserId(String userId, List<String> chosenIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT s.user_id,s.description,s.code,s.id,s.last,s.loaded,s.unloaded,s.actual,CASE WHEN usj.id IS NULL THEN 0 ELSE 1 END as marked FROM store as s Left JOIN (select * from store  as us where us.id in (");
    final int _inputSize = chosenIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")) as usj ON s.id=usj.id  Inner JOIN user_store as us ON s.id=us.id WHERE us.user_id=");
    _stringBuilder.append("?");
    _stringBuilder.append("  order by s.description");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 1 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : chosenIds) {
      if (_item == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _item);
      }
      _argIndex ++;
    }
    _argIndex = 1 + _inputSize;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfDescription = _cursor.getColumnIndexOrThrow("description");
      final int _cursorIndexOfCode = _cursor.getColumnIndexOrThrow("code");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfLast = _cursor.getColumnIndexOrThrow("last");
      final int _cursorIndexOfLoaded = _cursor.getColumnIndexOrThrow("loaded");
      final int _cursorIndexOfUnloaded = _cursor.getColumnIndexOrThrow("unloaded");
      final int _cursorIndexOfActual = _cursor.getColumnIndexOrThrow("actual");
      final int _cursorIndexOfMarked = _cursor.getColumnIndexOrThrow("marked");
      final List<Store> _result = new ArrayList<Store>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Store _item_1;
        _item_1 = new Store();
        _item_1.userId = _cursor.getString(_cursorIndexOfUserId);
        _item_1.description = _cursor.getString(_cursorIndexOfDescription);
        _item_1.code = _cursor.getString(_cursorIndexOfCode);
        _item_1.id = _cursor.getString(_cursorIndexOfId);
        _item_1.last = _cursor.getLong(_cursorIndexOfLast);
        _item_1.loaded = _cursor.getLong(_cursorIndexOfLoaded);
        _item_1.unloaded = _cursor.getLong(_cursorIndexOfUnloaded);
        _item_1.actual = _cursor.getInt(_cursorIndexOfActual);
        _item_1.marked = _cursor.getInt(_cursorIndexOfMarked);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Store> getChosenByUserId(String userId) {
    final String _sql = "SELECT s.* FROM store as s INNER JOIN user_store_join as usj ON s.id=usj.id WHERE usj.user_id=?";
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
  public List<String> getChosenIdsByUserId(String userId) {
    final String _sql = "SELECT s.id FROM store as s INNER JOIN user_store_join as usj ON s.id=usj.id WHERE usj.user_id=?";
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
  public ChosenStore get(String userId, String storeId) {
    final String _sql = "SELECT * FROM user_store_join WHERE user_store_join.user_id=? AND user_store_join.id =?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    if (storeId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, storeId);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfUserId = _cursor.getColumnIndexOrThrow("user_id");
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final ChosenStore _result;
      if(_cursor.moveToFirst()) {
        _result = new ChosenStore();
        _result.user_id = _cursor.getString(_cursorIndexOfUserId);
        _result.id = _cursor.getString(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
