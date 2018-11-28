package ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mark Khakimulin on 03.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class ConversionData implements Parcelable{

    public Date date;
    public int visitors;
    public int cheques;
    public int items;
    public String storeId;
    public String storeTitle;

    public ConversionData() {
    }


    public ConversionData(String storeId,String storeTitle,int visitors,int cheques,int items,Date date) {
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

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(date.getTime());
        out.writeInt(visitors);
        out.writeInt(cheques);
        out.writeInt(items);
        out.writeString(storeId);
        out.writeString(storeTitle);
    }

    public static final Parcelable.Creator<ConversionData> CREATOR
            = new Parcelable.Creator<ConversionData>() {
        public ConversionData createFromParcel(Parcel in) {
            return new ConversionData(in);
        }

        public ConversionData[] newArray(int size) {
            return new ConversionData[size];
        }
    };

    private ConversionData(Parcel in) {
        date = new Date(in.readLong());
        visitors= in.readInt();
        cheques= in.readInt();
        items= in.readInt();
        storeId = in.readString();
        storeTitle = in.readString();
    }


}
