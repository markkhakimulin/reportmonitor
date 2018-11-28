package ru.bashmag.khakimulin.reportmonitor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.db.tables.User;
import ru.bashmag.khakimulin.reportmonitor.db.tables.UserStore;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

@Dao
public interface UserStoreDao {

    @Query("SELECT * FROM store as s left join " +
            "(select * from user_store as us where us.user_id = :userId) us " +
            "on s.id = us.id order by description")
    List<Store> getAllStores(String userId);

    @Query("SELECT s.id FROM store as s left join " +
            "(select * from user_store as us where us.user_id = :userId) us " +
            "on s.id = us.id order by description")
    List<String> getAllStoreIds(String userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserStore user);

    @Query("DELETE from user_store where user_store.user_id = :userId and user_store.id not in (:ids)")
    void deleteAllExcept(String userId,List<String> ids);

    @Delete
    void delete(UserStore user);

    @Update
    void update(List<UserStore> list);


}
