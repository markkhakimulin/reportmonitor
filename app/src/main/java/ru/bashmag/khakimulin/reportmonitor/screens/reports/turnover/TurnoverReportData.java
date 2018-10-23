package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover;

import android.os.Parcel;
import android.os.Parcelable;

import ru.bashmag.khakimulin.reportmonitor.db.tables.Store;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;

/**
 * Created by Mark Khakimulin on 26.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverReportData extends ClassLoader  implements Parcelable {

    public TurnoverData monthly,daily;
    public ConversionData conversion;


    public TurnoverReportData() {}

    public TurnoverReportData(TurnoverData monthly, TurnoverData daily, ConversionData conversion) {

        this.monthly = monthly;
        this.daily = daily;
        this.conversion = conversion;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(monthly,flags);
        out.writeParcelable(daily,flags);
        out.writeParcelable(conversion,flags);
    }

    public static final Creator<TurnoverReportData> CREATOR
            = new Creator<TurnoverReportData>() {
        public TurnoverReportData createFromParcel(Parcel in) {
            return new TurnoverReportData(in);
        }

        public TurnoverReportData[] newArray(int size) {
            return new TurnoverReportData[size];
        }
    };

    private TurnoverReportData(Parcel in) {
        monthly = in.readParcelable(TurnoverData.class.getClassLoader());
        daily= in.readParcelable(TurnoverData.class.getClassLoader());
        conversion= in.readParcelable(ConversionData.class.getClassLoader());
    }

}
