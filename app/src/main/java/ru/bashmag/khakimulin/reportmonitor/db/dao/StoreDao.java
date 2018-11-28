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

    @Query("SELECT * FROM store order by description")
    List<Store> getAll();

    @Query("SELECT * FROM store WHERE id = :id")
    Store getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Store store);

    @Delete
    void deleteAll(List<Store> storeList);

    @Delete
    void delete(Store store);

    @Query("SELECT * FROM store limit 3")
    List<Store> getTop3();

}
