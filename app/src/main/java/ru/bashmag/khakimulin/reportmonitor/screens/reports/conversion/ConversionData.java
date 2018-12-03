package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.Date;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class ConversionData implements Parcelable{

    public static final Creator<ConversionData> CREATOR = new Creator<ConversionData>() {
        public ConversionData createFromParcel(Parcel in) {
            return new ConversionData(in);
        }

        public ConversionData[] newArray(int size) {
            return new ConversionData[size];
        }
    };
    public int cheques;
    public Date date;
    public int items;
    public String storeId;
    public String storeTitle;
    public int visitors;

    public ConversionData() {
        this.visitors = 0;
        this.cheques = 0;
        this.items = 0;
    }

    private ConversionData(Parcel in) {
        this.visitors = 0;
        this.cheques = 0;
        this.items = 0;
        this.date = new Date(in.readLong());
        this.visitors = in.readInt();
        this.cheques = in.readInt();
        this.items = in.readInt();
        this.storeId = in.readString();
        this.storeTitle = in.readString();
    }

    public ConversionData(String storeId, String storeTitle, int visitors, int cheques, int items, Date date) {
        this.visitors = 0;
        this.cheques = 0;
        this.items = 0;
        this.storeId = storeId;
        this.storeTitle = storeTitle;
        this.visitors = visitors;
        this.cheques = cheques;
        this.items = items;
        this.date = date;
    }

    public int describeContents() {
        return 0;
    }

    public boolean isEmpty() {

        if (!TextUtils.isEmpty(this.storeId)) {
            if (this.cheques != 0 || this.visitors != 0 || this.items != 0) {
                return false;
            }
        }
        return true;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.date.getTime());
        out.writeInt(this.visitors);
        out.writeInt(this.cheques);
        out.writeInt(this.items);
        out.writeString(this.storeId);
        out.writeString(this.storeTitle);
    }

}
