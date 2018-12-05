package ru.bashmag.khakimulin.reportmonitor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import ru.bashmag.khakimulin.reportmonitor.db.tables.ChosenStore;
import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

@Dao
public interface UserChosenStoresDao {

    @Query("DELETE FROM user_store_join WHERE user_store_join.user_id = :userId AND user_store_join.id = :storeId")
    int delete(String userId, String storeId);

    @Query("SELECT * FROM user_store_join WHERE user_store_join.user_id=:userId AND user_store_join.id =:storeId")
    ChosenStore get(String userId, String storeId);

    @Query("SELECT s.user_id,s.description,s.code,s.id,s.last,s.loaded,s.unloaded,s.actual," +
            "CASE WHEN usj.id IS NULL THEN 0 ELSE 1 END as marked " +
            "FROM store as s " +
            "Left JOIN (select * from user_store_join  as uj where uj.user_id = '00000000-0000-0000-0000-000000000000') as usj " +
            "ON s.id=usj.id order by s.description")
    List<Store> getAllByAnonymous();

    @Query("SELECT s.user_id,s.description,s.code,s.id,s.last,s.loaded,s.unloaded,s.actual," +
            "CASE WHEN usj.id IS NULL THEN 0 ELSE 1 END as marked " +
            "FROM store as s " +
            "Left JOIN (select * from store  as us where us.id in (:chosenIds)) as usj " +
            "ON s.id=usj.id order by s.description")
    List<Store> getAllByIdsByAnonymous(List<String> chosenIds);

    @Query("SELECT s.user_id,s.description,s.code,s.id,s.last,s.loaded,s.unloaded,s.actual," +
            "CASE WHEN usj.id IS NULL THEN 0 ELSE 1 END as marked FROM store as s " +
            "Left JOIN (select * from store  as us where us.id in (:chosenIds)) as usj ON s.id=usj.id  " +
            "Inner JOIN user_store as us " +
            "ON s.id=us.id WHERE us.user_id=:userId  order by s.description")
    List<Store> getAllByIdsByUserId(String userId, List<String> chosenIds);

    @Query("SELECT s.user_id,s.description,s.code,s.id,s.last,s.loaded,s.unloaded,s.actual," +
            "CASE WHEN usj.id IS NULL THEN 0 ELSE 1 END as marked FROM store as s " +
            "Left JOIN (select * from user_store_join  as uj where uj.user_id = :userId) as usj ON s.id=usj.id " +
            "Inner JOIN user_store as us " +
            "ON s.id=us.id WHERE us.user_id=:userId  order by s.description")
    List<Store> getAllByUserId(String userId);

    @Query("SELECT s.* FROM store as s INNER JOIN user_store_join as usj ON s.id=usj.id WHERE usj.user_id=:userId")
    List<Store> getChosenByUserId(String userId);

    @Query("SELECT s.id FROM store as s INNER JOIN user_store_join as usj ON s.id=usj.id WHERE usj.user_id=:userId")
    List<String> getChosenIdsByUserId(String userId);

    @Query("SELECT s.* FROM store as s INNER JOIN user_store_join as usj ON s.id=usj.id WHERE usj.user_id=:userId")
    Single<List<Store>> getChosenByUserIdObservable(String userId);

    @Query("SELECT s.id FROM store as s INNER JOIN user_store_join as usj ON s.id=usj.id WHERE usj.user_id=:userId")
    Single<List<String>> getChosenIdsByUserIdObservable(String userId);

    @Insert(onConflict = 1)
    void insert(ChosenStore chosenStore);
}
