package ru.bashmag.khakimulin.reportmonitor.screens.login;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;

/**
 * Created by Mark Khakimulin on 01.11.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class UserData implements Parcelable {

    public String id = "";
    public String title = "";

    public UserData() {

    }
    public UserData(String id) {
        this.id =id;
    }
    public UserData(String id, String title) {
        this.id =id;
        this.title = title;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(title);
    }

    public static final Creator<UserData> CREATOR
            = new Creator<UserData>() {
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    private UserData(Parcel in) {
        id = in.readString();
        title = in.readString();
    }
    public boolean isEmpty() {
        return TextUtils.isEmpty(id) || id.equals(Constants.EMPTY_ID);
    }

    public String toString() {
        return title;
    }
}
