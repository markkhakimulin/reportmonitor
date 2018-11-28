package ru.bashmag.khakimulin.reportmonitor.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import ru.bashmag.khakimulin.reportmonitor.db.tables.User;

/**
 * Created by Mark Khakimulin on 01.10.2018.
 * Email : mark.khakimulin@gmail.com
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM user order by description")
    List<User> getAll();

    @Query("SELECT user.id FROM user")
    List<String> getAllIds();

    @Query("SELECT * FROM user WHERE id = :id")
    User getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("DELETE from user where user.id != '00000000-0000-0000-0000-000000000000'")
    void deleteAll();

    @Update
    void update(List<User> list);

    @Update
    void update(User user);

    @Delete
    void delete(User user);
}
