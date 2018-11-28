package ru.bashmag.khakimulin.reportmonitor.db.tables;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Objects;

import ru.bashmag.khakimulin.reportmonitor.utils.Constants;

/**
 * Created by Mark Khakimulin on 12.11.2018.
 * Email : mark.khakimulin@gmail.com
 */
@Entity
public class User{

    @NonNull
    @PrimaryKey
    public String id;

    public String code;

    public String description;

    public User() {
    }

    public User(@NonNull String userId, String title) {
        id = userId;
        description = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

   /* @Ignore
    public int describeContents() {
        return 0;
    }
    @Ignore
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(description);
    }
    @Ignore
    public static final Creator<User> CREATOR
            = new Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @Ignore
    private User(Parcel in) {
        id = Objects.requireNonNull(in.readString());
        description = in.readString();
    }*/
    public boolean isEmpty() {
        return TextUtils.isEmpty(id) || id.equals(Constants.EMPTY_ID);
    }

    @NonNull
    public String toString() {
        return description;
    }
}
