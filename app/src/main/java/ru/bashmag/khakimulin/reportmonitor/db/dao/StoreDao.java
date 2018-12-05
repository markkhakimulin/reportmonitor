package ru.bashmag.khakimulin.reportmonitor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

@Dao
public interface StoreDao {

    @Delete
    void delete(Store store);

    @Query("delete FROM store WHERE store.id not in (:storeList)")
    void deleteAllExcept(List<String> storeList);

    @Query("SELECT * FROM store order by description")
    List<Store> getAll();

    @Query("SELECT * FROM store WHERE id = :storeId")
    Store getById(String storeId);

    @Query("SELECT * FROM store inner join user_store as us on store.id = us.id WHERE us.user_id = :userId")
    List<Store> getByUserId(String userId);

    @Query("SELECT * FROM store limit 3")
    List<Store> getTop3();

    @Insert(onConflict = 1)
    void insert(Store store);

}
