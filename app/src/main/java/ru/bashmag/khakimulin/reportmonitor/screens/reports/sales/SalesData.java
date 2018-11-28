package ru.bashmag.khakimulin.reportmonitor.screens.reports.sales;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;

/**
 * Created by Mark Khakimulin on 01.11.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class SalesData implements Parcelable {

    public String storeId;
    public String storeTitle;
    public String employee;
    public String position;
    public TurnoverData[] turnoverDataList;

    public SalesData() {

    }

    public SalesData(String storeId,String storeTitle,String employee,String position,TurnoverData[] turnoverDataList) {
        this.storeId =storeId;
        this.storeTitle = storeTitle;
        this.employee = employee;
        this.position = position;
        this.turnoverDataList = turnoverDataList;
    }

    public ArrayList<TurnoverData> getTurnoverList() {
        return  new ArrayList<>(Arrays.asList(turnoverDataList));
    }

    public double getTurnoverFact() {
        double res = 0;
        for (TurnoverData data:turnoverDataList) {
            res+=data.fact;
        }
        return res;
    }
    public double getTurnoverPlan() {
        double res = 0;
        for (TurnoverData data:turnoverDataList) {
            res+=data.plan;
        }
        return res;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(storeId);
        out.writeString(storeTitle);
        out.writeString(employee);
        out.writeString(position);
        out.writeParcelableArray(turnoverDataList,flags);
    }

    public static final Parcelable.Creator<SalesData> CREATOR
            = new Parcelable.Creator<SalesData>() {
        public SalesData createFromParcel(Parcel in) {
            return new SalesData(in);
        }

        public SalesData[] newArray(int size) {
            return new SalesData[size];
        }
    };

    private SalesData(Parcel in) {
        storeId = in.readString();
        storeTitle = in.readString();
        employee = in.readString();
        position = in.readString();
        turnoverDataList = (TurnoverData[])in.readParcelableArray(TurnoverData.class.getClassLoader());
    }

}
