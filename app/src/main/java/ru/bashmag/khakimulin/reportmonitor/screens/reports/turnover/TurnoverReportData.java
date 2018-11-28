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

    public TurnoverData turnover,turnoverLast;
    public ConversionData conversionLast;


    public TurnoverReportData() {}

    public TurnoverReportData(TurnoverData monthly, TurnoverData daily, ConversionData conversion) {

        this.turnover = monthly;
        this.turnoverLast = daily;
        this.conversionLast = conversion;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(turnover,flags);
        out.writeParcelable(turnoverLast,flags);
        out.writeParcelable(conversionLast,flags);
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
        turnover = in.readParcelable(TurnoverData.class.getClassLoader());
        turnoverLast= in.readParcelable(TurnoverData.class.getClassLoader());
        conversionLast= in.readParcelable(ConversionData.class.getClassLoader());
    }

}
