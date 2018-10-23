package ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.bashmag.khakimulin.reportmonitor.R;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list.ReportAdapter;
import ru.bashmag.khakimulin.reportmonitor.screens.reportlist.list.ReportItem;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.conversion.ConversionData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverData;
import ru.bashmag.khakimulin.reportmonitor.screens.reports.turnover.TurnoverReportData;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Mark Khakimulin on 26.10.2018.
 * Email : mark.khakimulin@gmail.com
 */
public class TurnoverAdapter extends RecyclerView.Adapter<TurnoverAdapter.ReportViewHolder>{

    private final PublishSubject<TurnoverData> monthFactClicks = PublishSubject.create();
    private final PublishSubject<TurnoverData> monthPlanClicks = PublishSubject.create();
    private final PublishSubject<TurnoverData> dailyFactClicks = PublishSubject.create();
    private final PublishSubject<TurnoverData> dailyPlanClicks = PublishSubject.create();

    private final PublishSubject<ConversionData> conversionClicks = PublishSubject.create();
    private final PublishSubject<ConversionData> fullnessClicks = PublishSubject.create();
    private ArrayList<TurnoverReportData> objects = new ArrayList<>();

    @NonNull
    @Override
    public TurnoverAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_turnover, viewGroup, false);
        return new TurnoverAdapter.ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoverAdapter.ReportViewHolder reportViewHolder, int position) {
        TurnoverReportData report = objects.get(position);
        reportViewHolder.bind(report);
    }

    @Override
    public int getItemCount() {
        if (objects != null && objects.size() > 0) {
            return objects.size();
        } else {
            return 0;
        }
    }

    public void replaceAll(ArrayList<TurnoverReportData> list) {
        objects.clear();
        objects.addAll(list);
        notifyDataSetChanged();
    }

    public Observable<TurnoverData> observeMonthFactClicks() {
        return monthFactClicks;
    }
    public Observable<TurnoverData> observeMonthPlanClicks() {
        return monthPlanClicks;
    }
    public Observable<TurnoverData> observeDailyFactClicks() {
        return dailyFactClicks;
    }
    public Observable<TurnoverData> observeDailyPlanClicks() {
        return dailyPlanClicks;
    }
    public Observable<ConversionData> observeConversionClicks() {
        return conversionClicks;
    }
    public Observable<ConversionData> observeFullnessClicks() {
        return fullnessClicks;
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {

        View view;

        @BindView(R.id.store)
        TextView store;

        @BindView(R.id.month_plan)
        TextView monthPlan;

        @BindView(R.id.month_fact)
        TextView monthFact;

        @BindView(R.id.daily_plan)
        TextView dailyPlan;

        @BindView(R.id.daily_fact)
        TextView dailyFact;

        @BindView(R.id.conversion)
        TextView conversion;

        @BindView(R.id.fullness)
        TextView fullness;

        public ReportViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, view);

            monthPlan.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monthPlanClicks.onNext(objects.get(getAdapterPosition()).monthly);
                }
            });
            monthFact.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    monthFactClicks.onNext(objects.get(getAdapterPosition()).monthly);
                }
            });
            dailyPlan.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyPlanClicks.onNext(objects.get(getAdapterPosition()).daily);
                }
            });
            dailyFact.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dailyFactClicks.onNext(objects.get(getAdapterPosition()).daily);
                }
            });
            conversion.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    conversionClicks.onNext(objects.get(getAdapterPosition()).conversion);
                }
            });
            fullness.getRootView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullnessClicks.onNext(objects.get(getAdapterPosition()).conversion);
                }
            });
        }

        void bind(TurnoverReportData reportItem) {

            DecimalFormat df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.CEILING);

            store.setText(reportItem.monthly.storeTitle);
            monthPlan.setText(String.valueOf(reportItem.monthly.plan));
            monthFact.setText(String.valueOf(reportItem.monthly.fact));
            dailyPlan.setText(String.valueOf(reportItem.daily.plan));
            dailyFact.setText(String.valueOf(reportItem.daily.fact));
            conversion.setText(String.valueOf(df.format(reportItem.conversion.cheques/reportItem.conversion.visitors*100)));
            fullness.setText(String.valueOf(df.format(reportItem.conversion.items/reportItem.conversion.cheques*100)));
        }

    }
}
