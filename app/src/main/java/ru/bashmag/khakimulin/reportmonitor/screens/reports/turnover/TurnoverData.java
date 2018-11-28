package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.Date;

import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.utils.Constants;

/**
 * Created by Mark Khakimulin on 26.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverData implements Parcelable {

    public double plan = 0,fact = 0;
    public Date date;
    public String storeId;
    public String storeTitle;
    public String group = "";
    public int actual = 1;

    public TurnoverData() {

    }
    public TurnoverData(String storeId,String storeTitle,float plan,float fact,Date date) {
        this.storeId = storeId;
        this.storeTitle = storeTitle;
        this.plan = plan;
        this.fact = fact;
        this.date = date;

    }
    public TurnoverData(String storeId,String storeTitle,float plan,float fact,String group ) {
        this.storeId = storeId;
        this.storeTitle = storeTitle;
        this.plan = plan;
        this.fact = fact;
        this.group = group;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(storeId);
        out.writeString(storeTitle);
        out.writeDouble(plan);
        out.writeDouble(fact);
        out.writeString(group);
        out.writeLong(date.getTime());
        out.writeInt(actual);
    }

    public static final Parcelable.Creator<TurnoverData> CREATOR
            = new Parcelable.Creator<TurnoverData>() {
        public TurnoverData createFromParcel(Parcel in) {
            return new TurnoverData(in);
        }

        public TurnoverData[] newArray(int size) {
            return new TurnoverData[size];
        }
    };

    private TurnoverData(Parcel in) {
        storeId = in.readString();
        storeTitle= in.readString();
        plan = in.readDouble();
        fact= in.readInt();
        group= in.readString();
        date= new Date(in.readLong());
        actual = in.readInt();
    }
    public boolean isEmpty() {
        return TextUtils.isEmpty(storeId) || (plan ==0 && fact==0);
    }

    public boolean isActual() {
        return actual == 1 ;
    }

}
